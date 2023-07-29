package kr.zb.nengtul.user.domain.dto;

import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.user.domain.entity.User;
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
public class UserDetailDto {
  private String name;
  private String nickname;
  private String password; //비밀번호 변경을 위해서
  private String phoneNumber;
  private String profileImageUrl;
  private ProviderType providerTYpe;
  private String address;
  private String addressDetail;
  private boolean emailVerifiedYn;
  private int point;

  public static UserDetailDto buildUserDetailDto(User user){
    return UserDetailDto.builder()
        .name(user.getName())
        .nickname(user.getNickname())
        .password(user.getPassword())
        .phoneNumber(user.getPhoneNumber())
        .profileImageUrl(user.getProfileImageUrl())
        .providerTYpe(user.getProviderType())
        .address(user.getAddress())
        .addressDetail(user.getAddressDetail())
        .emailVerifiedYn(user.isEmailVerifiedYn())
        .point(user.getPoint())
        .build();
  }
}
