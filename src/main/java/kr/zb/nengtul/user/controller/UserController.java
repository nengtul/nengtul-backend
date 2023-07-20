package kr.zb.nengtul.user.controller;

import java.security.Principal;
import kr.zb.nengtul.user.entity.dto.UserDetailDto;
import kr.zb.nengtul.user.entity.dto.UserFindEmailDto;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserUpdateDto;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  //회원가입
  @PostMapping("/join")
  public ResponseEntity<String> join(@RequestBody UserJoinDto userJoinDto) {
    return ResponseEntity.ok(userService.join(userJoinDto));
  }

  //이메일 인증
  @PutMapping("/verify")
  public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String code) {
    userService.verify(email, code);
    return ResponseEntity.ok("인증이 완료되었습니다.");
  }

  //이메일 인증번호 재발급 요청
  @PostMapping("/verify/reset/{userId}")
  public ResponseEntity<String> resetVerify(@PathVariable Long userId) {
    userService.resetVerify(userId);
    return ResponseEntity.ok("코드 재발급 및 연장이 완료되었습니다.");
  }

  //아이디 찾기
  @GetMapping("/findid")
  public ResponseEntity<String> findEmail(@RequestBody UserFindEmailDto userFindEmailDto) {
    return ResponseEntity.ok(userService.findEmail(userFindEmailDto));
  }

  //임시 비밀번호 발급
  @GetMapping("/findpw")
  public ResponseEntity<String> findPasswordForSendMail(@RequestParam String email) {
    return ResponseEntity.ok(userService.findPasswordForSendMail(email));
  }

  //회원 상세보기
  @GetMapping("/detail")
  public ResponseEntity<UserDetailDto> getUserDetail(Principal principal) {
    return ResponseEntity.ok(userService.getUserDetail(principal));
  }

  //회원 탈퇴(상세보기 페이지에서 진행)
  @DeleteMapping("/detail")
  public ResponseEntity<Void> quit(Principal principal) {
    userService.quit(principal);
    return ResponseEntity.ok(null);
  }

  //회원 정보 수정(상세보기 페이지에서 진행)
  @PutMapping("/detail")
  public ResponseEntity<String> update(Principal principal,
      @RequestBody UserUpdateDto userUpdateDto) {
    return ResponseEntity.ok(userService.update(principal, userUpdateDto));
  }
}
