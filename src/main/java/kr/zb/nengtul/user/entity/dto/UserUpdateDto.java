package kr.zb.nengtul.user.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
  private String nickname;
  private String password;
  private String phoneNumber;
  private String profileImageUrl;
  private String address;
  private String addressDetail;
}


