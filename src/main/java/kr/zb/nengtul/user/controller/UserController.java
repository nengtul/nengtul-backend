package kr.zb.nengtul.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.security.Principal;
import kr.zb.nengtul.user.domain.dto.UserDetailDto;
import kr.zb.nengtul.user.domain.dto.UserFindEmailReqDto;
import kr.zb.nengtul.user.domain.dto.UserFindEmailResDto;
import kr.zb.nengtul.user.domain.dto.UserFindPasswordDto;
import kr.zb.nengtul.user.domain.dto.UserJoinDto;
import kr.zb.nengtul.user.domain.dto.UserPasswordChangeDto;
import kr.zb.nengtul.user.domain.dto.UserUpdateDto;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "USER API", description = "회원 관련 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;

  //회원가입
  @Operation(summary = "회원가입", description = "회원가입 요청을 보냅니다.")
  @PostMapping("/join")
  public ResponseEntity<Void> joinUser(@RequestBody @Valid UserJoinDto userJoinDto) {
    userService.joinUser(userJoinDto);
    userService.verifyEmailForm(userJoinDto.getEmail(), userJoinDto.getName());

    return ResponseEntity.ok(null);
  }

  //이메일 인증 (이메일에서 링크를 클릭하여 put 요청을 보낼 수 없어서 GET요청으로 처리)
  @Operation(summary = "이메일 인증", description = "이메일을 통해 보낸 링크 클릭시 인증이 진행됩니다.")
  @GetMapping("/verify")
  public ResponseEntity<String> verifyEmail(
      @Parameter(name = "email", description = "이메일") @RequestParam String email,
      @Parameter(name = "code", description = "인증 코드") @RequestParam String code) {
    userService.verifyEmail(email, code);
    return ResponseEntity.ok("이메일 인증이 완료되었습니다. 냉장고를 털어라 홈페이지로 돌아가세요.");
  }

  //이메일 인증번호 재발급 요청
  @Operation(summary = "이메일 인증번호 재발급", description = "이메일 인증 시간을 증가시키며, 인증번호를 재발급합니다.")
  @PostMapping("/{userId}/verify/reset")
  public ResponseEntity<Void> resetVerify(
      @Parameter(name = "userId", description = "회원 아이디") @PathVariable Long userId) {
    userService.resetVerify(userId);
    return ResponseEntity.ok(null);
  }

  //아이디 찾기
  @Operation(summary = "아이디(이메일) 찾기", description = "아이디(이메일) 찾기 요청을 보냅니다.")
  @PostMapping("/findid")
  public ResponseEntity<UserFindEmailResDto> findEmail(
      @RequestBody UserFindEmailReqDto userFindEmailReqDto) {
    return ResponseEntity.ok(
        new UserFindEmailResDto(userService.findEmail(userFindEmailReqDto).getEmail()));
  }

  //임시 비밀번호 발급
  @Operation(summary = "비밀번호 재발급", description = "임시 비밀번호를 이메일로 전송합니다.")
  @Parameter(name = "비밀번호 재발급 DTO", description = "비밀번호 재발급 DTO")
  @PostMapping("/findpw")
  public ResponseEntity<Void> getNewPassword(@RequestBody UserFindPasswordDto userFindPasswordDto) {
    userService.getNewPassword(userFindPasswordDto);
    return ResponseEntity.ok(null);
  }

  //회원 상세보기
  @Operation(summary = "회원 상세보기", description = "토큰을 통해 회원 상세 정보를 받습니다.")
  @GetMapping("/detail")
  public ResponseEntity<UserDetailDto> getUserDetail(Principal principal) {
    return ResponseEntity.ok(
        userService.buildUserDetailDto(userService.findUserByEmail(principal.getName())));
  }

  //회원 탈퇴(상세보기 페이지에서 진행)
  @Operation(summary = "회원 탈퇴", description = "토큰을 통해 회원 탈퇴 요청을 보냅니다.")
  @DeleteMapping("/detail")
  public ResponseEntity<Void> quitUser(Principal principal) {
    userService.quitUser(principal);
    return ResponseEntity.ok(null);
  }

  //로그아웃
  @Operation(summary = "로그아웃", description = "사용하던 토큰을 블랙리스트로 등록해 다시 사용할 수 없게 지정합니다.")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, Principal principal) {
    userService.logout(request,principal);
    return ResponseEntity.ok(null);
  }

  //회원 정보 수정(상세보기 페이지에서 진행)
  @Operation(summary = "회원 정보 수정", description = "토큰을 통해 회원을 인식하고, 회원에 대한 정보를 수정합니다.")
  @PostMapping("/detail")
  public ResponseEntity<Void> updateUser(Principal principal,
      @RequestPart(value = "userUpdateDto")  @Valid UserUpdateDto userUpdateDto,
      @RequestPart(value = "image", required = false) MultipartFile image) {
    userService.updateUser(principal, userUpdateDto, image);
    return ResponseEntity.ok(null);
  }
  @Operation(summary = "회원 비밀번호 변경", description = "토큰을 통해 회원을 인식하고, 회원에 대한 비밀번호를 수정합니다.")
  @PutMapping("/detail/password")
  public ResponseEntity<Void> changePassword(Principal principal,
      @RequestBody @Valid UserPasswordChangeDto userPasswordChangeDto) {
    userService.changePassword(principal, userPasswordChangeDto);
    return ResponseEntity.ok(null);
  }
}
