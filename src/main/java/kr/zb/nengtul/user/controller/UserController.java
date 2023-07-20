package kr.zb.nengtul.user.controller;

import java.security.Principal;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserUpdateDto;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/nengtul/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<String> join(@RequestBody UserJoinDto userJoinDto){
    return ResponseEntity.ok(userService.join(userJoinDto));
  }

  @PutMapping("/verify")
  public ResponseEntity<String> verify(@RequestParam String email,@RequestParam String code){
    userService.verify(email,code);
    return ResponseEntity.ok("인증이 완료되었습니다.");
  }

  @PostMapping("/verify/reset")
  public ResponseEntity<String> resetVerify(Principal principal){
    userService.resetVerify(principal);
    return ResponseEntity.ok("코드 재발급 및 연장이 완료되었습니다.");
  }


  @PostMapping("/quit")
  public ResponseEntity<Void> quit(Principal principal){
    userService.quit(principal);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/update")
  public ResponseEntity<String> update(Principal principal,@RequestBody UserUpdateDto userUpdateDto){
    return ResponseEntity.ok(userService.update(principal, userUpdateDto));
  }

}
