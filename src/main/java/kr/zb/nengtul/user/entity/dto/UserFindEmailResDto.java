package kr.zb.nengtul.user.entity.dto;

import kr.zb.nengtul.user.entity.domain.User;
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
public class UserFindEmailResDto {

  private String email;

  public static UserFindEmailResDto buildUserFindEmailResDto(String email) {
    return UserFindEmailResDto.builder()
        .email(email)
        .build();
  }
}

