package kr.zb.nengtul.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @GetMapping("/refresh")
  public ResponseEntity<String> refreshToken (){
    //access Token 및 refreshToken 발급 과정은 필터에서 진행, 단순 요청을 위한 api\
    return null;
  }
}
