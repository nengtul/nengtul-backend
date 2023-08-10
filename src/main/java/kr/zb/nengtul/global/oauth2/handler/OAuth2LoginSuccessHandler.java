package kr.zb.nengtul.global.oauth2.handler;


import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_USER;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.global.oauth2.CustomOAuth2User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("OAuth2 Login 성공");
    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

      // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
      String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getEmail());
      response.addHeader(jwtTokenProvider.getAccessHeader(), "Bearer " + accessToken);
      String refreshToken = jwtTokenProvider.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급
      userRepository.findByEmail(oAuth2User.getEmail())
          .ifPresent(user -> {
            user.updateRefreshToken(refreshToken);
            userRepository.saveAndFlush(user);
          });
//      jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, null);
      loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
    } catch (CustomException e) {
      throw new CustomException(NOT_FOUND_USER);
    }

  }

  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
      throws IOException {
    String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getEmail());
    String refreshToken = jwtTokenProvider.createRefreshToken();
    response.addHeader(jwtTokenProvider.getAccessHeader(), "Bearer " + accessToken);
    response.addHeader(jwtTokenProvider.getRefreshHeader(), "Bearer " + refreshToken);

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtTokenProvider.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
  }
}