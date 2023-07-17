package kr.zb.nengtul.user.entity.dto;

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
  private String name;
  private String nickname;
  private String password;
  private String phoneNumber;
  private String email;
  private String address;
  private String addressDetail;
}


