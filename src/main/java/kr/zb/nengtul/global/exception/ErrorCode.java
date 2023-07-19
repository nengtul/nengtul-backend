package kr.zb.nengtul.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  // 유저
  ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 등록 되어있는 사용자입니다. "),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  CHECK_ID_AND_PW(HttpStatus.NOT_FOUND, "이메일 혹은 비밀번호를 확인하세요. "),
  CHECK_SOCIAL_SERVER(HttpStatus.NOT_FOUND, "소셜로그인에 실패하였습니다. 서버를 확인 하세요. "),
  SHORT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이여야 합니다. "),
  NO_PERMISSION(HttpStatus.BAD_REQUEST, "권한이 없습니다.");

  //게시판

  //

  private final HttpStatus httpStatus;
  private final String detail;
}