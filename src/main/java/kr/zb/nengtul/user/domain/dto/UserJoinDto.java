package kr.zb.nengtul.user.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.EMAIL_FORMAT_NOT_CORRECT_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.EMAIL_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LONG_NAME_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LONG_NICKNAME_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.NAME_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.NICKNAME_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PASSWORD_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PHONE_NUMBER_FORMAT_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PHONE_NUMBER_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.SHORT_PASSWORD_LENGTH_MESSAGE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinDto {

  @NotEmpty(message = NAME_NOT_NULL_MESSAGE)
  @Size(max = 10, message = LONG_NAME_MESSAGE)
  private String name;

  @NotEmpty(message = NICKNAME_NOT_NULL_MESSAGE)
  @Size(max = 15, message = LONG_NICKNAME_MESSAGE)
  private String nickname;

  @NotEmpty(message = PASSWORD_NOT_NULL_MESSAGE)
  @Size(min = 8, message = SHORT_PASSWORD_LENGTH_MESSAGE)
  private String password;

  @NotEmpty(message = PHONE_NUMBER_NOT_NULL_MESSAGE)
  @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = PHONE_NUMBER_FORMAT_MESSAGE)
  private String phoneNumber;

  @NotEmpty(message = EMAIL_NOT_NULL_MESSAGE)
  @Email(message = EMAIL_FORMAT_NOT_CORRECT_MESSAGE)
  private String email;

  private String address;
  private String addressDetail;
}


