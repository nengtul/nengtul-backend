package kr.zb.nengtul.user.controller;

import java.security.Principal;
import java.util.Map;
import kr.zb.nengtul.global.jwt.JwtToken;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserLoginDto;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/nengtul/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<String> join(@RequestBody UserJoinDto userJoinDto) throws Exception {
    return ResponseEntity.ok(userService.join(userJoinDto));
  }

//  @PostMapping("/login")
//  public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto) throws Exception {
//
//    return ResponseEntity.ok(userService.login(userLoginDto));
//  }

  @PostMapping("/quit")
  public ResponseEntity<Void> quit(Principal principal) throws Exception {
    userService.quit(principal);
    return ResponseEntity.ok(null);
  }

}
