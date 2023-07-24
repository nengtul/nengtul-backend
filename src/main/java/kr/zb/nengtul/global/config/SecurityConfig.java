package kr.zb.nengtul.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import kr.zb.nengtul.global.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import kr.zb.nengtul.global.handler.LoginFailureHandler;
import kr.zb.nengtul.global.handler.LoginSuccessHandler;
import kr.zb.nengtul.global.jwt.JwtAuthenticationProcessingFilter;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.global.jwt.service.CustomUserDetailService;
import kr.zb.nengtul.global.oauth2.handler.OAuth2LoginFailureHandler;
import kr.zb.nengtul.global.oauth2.handler.OAuth2LoginSuccessHandler;
import kr.zb.nengtul.global.oauth2.service.CustomOAuth2UserService;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailService customUserDetailService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(frameOptions -> headers.disable()))
        .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        //== URL별 권한 관리 옵션 ==//
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico",
                "/h2-console/**",
                "/index.html",
                "/login/**",
                "/v1/user/join",//회원가입
                "/v1/user/login",//로그인
                "/v1/user/findpw",//비밀번호 찾기 (비밀번호 재발급)
                "/v1/user/findid",//아이디 찾기
                "/v1/user/verify/**", //이메일 인증
                "/v1/notice/list/**" //공지사항 조회관련
            ).permitAll()
            .requestMatchers("/v1/user/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(
                "/v1/admin/**",
                "/v1/notice/**"
            ).hasRole("ADMIN")
            .anyRequest().permitAll() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
        )
        //== 소셜 로그인 설정 ==//
        .oauth2Login(oauth2Configurer -> oauth2Configurer.loginPage("/")
            .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
            .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
            .userInfoEndpoint(
                userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                    customOAuth2UserService)));

    // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(),
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
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(
        jwtTokenProvider, userRepository);
  }
}