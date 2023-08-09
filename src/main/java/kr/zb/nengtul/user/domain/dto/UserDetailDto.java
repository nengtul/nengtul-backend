package kr.zb.nengtul.user.domain.dto;

import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.global.entity.RoleType;
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
  private Long id;
  private String name;
  private String nickname;
  private String phoneNumber;
  private String profileImageUrl;
  private ProviderType providerTYpe;
  private String address;
  private String addressDetail;
  private RoleType roles;
  private boolean emailVerifiedYn;
  private int point;

  public static UserDetailDto buildUserDetailDto(User user){
    return UserDetailDto.builder()
        .id(user.getId())
        .name(user.getName())
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .profileImageUrl(user.getProfileImageUrl())
        .providerTYpe(user.getProviderType())
        .address(user.getAddress())
        .addressDetail(user.getAddressDetail())
        .roles(user.getRoles())
        .emailVerifiedYn(user.isEmailVerifiedYn())
        .point(user.getPoint())
        .build();
  }
}
