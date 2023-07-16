package kr.zb.nengtul.global.config;

import kr.zb.nengtul.global.jwt.JwtAuthenticationFilter;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig { //WebSecurityConfigurerAdapter was deprecated

  private final JwtTokenProvider jwtTokenProvider;


  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin().disable()
        .httpBasic().disable()
        .authorizeHttpRequests()
//        .requestMatchers("/v1/nengtul/user/quit").hasRole("USER")
        .requestMatchers("/v1/nengtul/user/quit").hasRole("ADMIN")
        .requestMatchers("/v1/nengtul/user/login", "/v1/nengtul/user/join").permitAll()
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}

