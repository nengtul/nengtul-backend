package kr.zb.nengtul.global.handler;

import static kr.zb.nengtul.global.exception.ErrorCode.CHECK_ID_AND_PW;

import com.nimbusds.oauth2.sdk.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  @Value("${spring.jwt.access.expiration}")
  private String accessTokenExpiration;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
    String accessToken = jwtTokenProvider.createAccessToken(
        email); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
    String refreshToken = jwtTokenProvider.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken,
        refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답

    userRepository.findByEmail(email)
        .ifPresent(user -> {
          user.updateRefreshToken(refreshToken);
          userRepository.saveAndFlush(user);
        });
//    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
//    log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
//    log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

    response.setStatus(HttpStatus.OK.value());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    String successMessage = "{\"email\": \"" + email +
        "\", \"AccessToken\": \"" + accessToken +
        "\", \"refreshToken\": \"" + refreshToken + "\"}";
    response.getWriter().write(successMessage);
  }

  private String extractUsername(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }
}