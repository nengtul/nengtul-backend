package kr.zb.nengtul.global.handler;

import static kr.zb.nengtul.global.exception.ErrorCode.CHECK_ID_AND_PW;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    response.setStatus(CHECK_ID_AND_PW.getHttpStatus().value());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    String errorMessage = "{\"status\": " + CHECK_ID_AND_PW.getHttpStatus().value() +
        ", \"code\": \"" + "CHECK_ID_AND_PW" +
        "\", \"message\": \"" + CHECK_ID_AND_PW.getDetail() + "\"}";

    response.getWriter().write(errorMessage);
    log.info("로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
  }
}