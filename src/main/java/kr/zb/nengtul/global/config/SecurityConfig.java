package kr.zb.nengtul.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.zb.nengtul.auth.repository.BlacklistTokenRepository;
import kr.zb.nengtul.global.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import kr.zb.nengtul.global.handler.LoginFailureHandler;
import kr.zb.nengtul.global.handler.LoginSuccessHandler;
import kr.zb.nengtul.global.handler.TokenAccessDeniedHandler;
import kr.zb.nengtul.global.jwt.JwtAuthenticationProcessingFilter;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.global.jwt.service.CustomUserDetailService;
import kr.zb.nengtul.global.oauth2.exception.RestAuthenticationEntryPoint;
import kr.zb.nengtul.global.oauth2.handler.OAuth2LoginFailureHandler;
import kr.zb.nengtul.global.oauth2.handler.OAuth2LoginSuccessHandler;
import kr.zb.nengtul.global.oauth2.service.CustomOAuth2UserService;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailService customUserDetailService;
  private final JwtTokenProvider jwtTokenProvider;
  private final BlacklistTokenRepository blacklistTokenRepository;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(frameOptions -> headers.disable()))
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(tokenAccessDeniedHandler))
        //== URL별 권한 관리 옵션 ==//
//        .authorizeHttpRequests(m -> methodSecurityExpressionHandler(new RoleHierarchy()))
        .authorizeHttpRequests(authorizationHttpRequests -> authorizationHttpRequests
            .requestMatchers("/v1/notices/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/v1/noticelist/**").permitAll()//공지사항 조회
            .requestMatchers(HttpMethod.GET, "/v1/recipe/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/v1/recipes/**").permitAll() //댓글
            .requestMatchers(HttpMethod.GET, "/v1/comments/**").permitAll()//대댓글
            .requestMatchers(
                // -- Swagger UI v2
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                // -- Swagger UI v3 (OpenAPI)
                "/v3/api-docs/**",
                "/swagger-ui/**").permitAll()
            .requestMatchers(
                "/**",
                "/v1/auth/**",
                "/v1/users/join",//회원가입
                "/v1/users/login",//로그인
                "/v1/users/findpw",//비밀번호 찾기 (비밀번호 재발급)
                "/v1/users/findid",//아이디 찾기
                "/v1/users/verify/**", //이메일 인증
                "/v1/recipe/commentlist/**", //댓글 조회
                "/chat/**"
            ).permitAll()
            .requestMatchers(
                "/v1/users/**",
                "/v1/shareboards/**",
                "/v1/recipe/**",
                "/v1/comments/**",//대댓글
                "/v1/recipes/comment/**",//댓글 작성,수정,삭제
                "/v1/likes/**",
                "/v1/favorite/**",
                "/v1/saved-recipe/**",
                "/v1/chat/**"
            ).hasRole("USER")
            .anyRequest().authenticated()) // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
        .logout(logout -> logout.logoutSuccessUrl("/"))
        //== 소셜 로그인 설정 ==//
        .oauth2Login(oauth2Login -> oauth2Login
            .authorizationEndpoint(
                authorizationEndpoint -> authorizationEndpoint.baseUri("/oauth2/authorize"))
            .redirectionEndpoint(
                redirectionEndpoint -> redirectionEndpoint.baseUri("/*/oauth2/code/*"))
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                customOAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler)
            .failureHandler(oAuth2LoginFailureHandler)
        )
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        .addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
        .addFilterBefore(jwtAuthenticationProcessingFilter(),
            CustomJsonUsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  //password Encoder
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(customUserDetailService);
    return new ProviderManager(provider);
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtTokenProvider, userRepository);
  }

  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  //커스텀 필터 및 성공 실패 핸들러
  @Bean
  public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
    CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
        = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
    customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(
        loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(
        loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(
        jwtTokenProvider, userRepository, blacklistTokenRepository);
  }

  //cors 설정
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
    return roleHierarchy;
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }
}