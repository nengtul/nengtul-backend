package kr.zb.nengtul.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AUTH API", description = "리프레시 토큰 API")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  @GetMapping("/refresh")
  @Operation(summary = "토큰 리프레시", description = "기능은 필터에서 동작하고 refresh 요청주소로 사용하기 위해 작성하였습니다.")
  public ResponseEntity<String> refreshToken() {
    //access Token 및 refreshToken 발급 과정은 필터에서 진행, 단순 요청을 위한 api\
    return null;
  }

  //healthy 판정위한 200api
  @GetMapping("/hello")
  @Operation(summary = "인증서 healthy 위함", description = "https 적용시 사용하였으며, 200을 반환하여 health 상태를 얻습니다..")
  public ResponseEntity<Void> hello(){
    return null;
  }

}

