package kr.zb.nengtul.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  // 유저
  ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 등록 되어있는 사용자입니다."),
  ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 등록 되어있는 이메일입니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증이 완료된 사용자입니다."),
  WRONG_VERIFY_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
  EXPIRED_CODE(HttpStatus.BAD_REQUEST, "인증시간이 만료되었습니다."),
  CHECK_ID_AND_PW(HttpStatus.NOT_FOUND, "이메일 혹은 비밀번호를 확인하세요."),
  CHECK_SOCIAL_SERVER(HttpStatus.NOT_FOUND, "소셜로그인에 실패하였습니다. 서버를 확인 하세요."),
  SHORT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상이여야 합니다."),

  //게시판

  //공지사항
  NOT_FOUND_NOTICE(HttpStatus.NOT_FOUND, "공지를 찾을 수 없습니다."),

  //나눔 게시판
  NOT_FOUND_SHARE_BOARD(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),

  //공통
  NOT_NULL_TITLE(HttpStatus.BAD_REQUEST, "제목을 입력해 주세요."),
  NOT_NULL_CONTENT(HttpStatus.BAD_REQUEST, "내용을 입력해 주세요."),
  NO_CONTENT(HttpStatus.NO_CONTENT, "일치하는 내용이 없습니다."),
  NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  NOT_VERIFY_EMAIL(HttpStatus.FORBIDDEN, "이메일 인증을 하지 않아 작성 권한이 없습니다.");

  //ExceptionHandler 에서 MethodArgumentNotValidException 용으로 사용
  //유저
  public static final String NAME_NOT_NULL_MESSAGE = "이름을 입력해 주세요.";
  public static final String NICKNAME_NOT_NULL_MESSAGE = "닉네임을 입력해 주세요.";
  public static final String SHORT_PASSWORD_LENGTH_MESSAGE = "비밀번호는 8자 이상이여야 합니다.";
  public static final String PASSWORD_NOT_NULL_MESSAGE = "비밀번호를 입력해 주세요.";
  public static final String PHONE_NUMBER_NOT_NULL_MESSAGE = "휴대폰 번호를 입력해 주세요.";
  public static final String PHONE_NUMBER_FORMAT_MESSAGE = "휴대폰 번호의 형식은 ex:010-1234-1234 형식이여야 합니다.";
  public static final String EMAIL_NOT_NULL_MESSAGE = "이메일을 입력해 주세요.";
  public static final String EMAIL_FORMAT_NOT_CORRECT_MESSAGE = "이메일 형식은 abc@def.gh 형식이여야 합니다.";

  //공통
  public static final String TITLE_NOT_NULL_MESSAGE = "제목을 입력해 주세요.";
  public static final String CONTENT_NOT_NULL_MESSAGE = "내용을 입력해 주세요.";

  //나눔게시판
  public static final String LAT_NOT_NULL_MESSAGE = "위도를 입력해 주세요.";
  public static final String LON_NOT_NULL_MESSAGE = "경도을 입력해 주세요.";


  private final HttpStatus httpStatus;
  private final String detail;
}