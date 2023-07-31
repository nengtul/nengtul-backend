package kr.zb.nengtul.user.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.PASSWORD_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.SHORT_PASSWORD_LENGTH_MESSAGE;

import jakarta.validation.constraints.NotEmpty;
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
public class UserPasswordChangeDto {
  @NotEmpty(message = PASSWORD_NOT_NULL_MESSAGE)
  @Size(min = 8, message = SHORT_PASSWORD_LENGTH_MESSAGE)
  private String password;
}
