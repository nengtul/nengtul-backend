package kr.zb.nengtul.user.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.NICKNAME_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PASSWORD_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PHONE_NUMBER_FORMAT_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.PHONE_NUMBER_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.SHORT_PASSWORD_LENGTH_MESSAGE;

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
public class UserUpdateDto {
  @NotEmpty(message = NICKNAME_NOT_NULL_MESSAGE)
  private String nickname;

  @NotEmpty(message = PHONE_NUMBER_NOT_NULL_MESSAGE)
  @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = PHONE_NUMBER_FORMAT_MESSAGE)
  private String phoneNumber;
  private String address;
  private String addressDetail;
}


