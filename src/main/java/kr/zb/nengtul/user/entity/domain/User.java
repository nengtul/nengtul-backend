package kr.zb.nengtul.user.entity.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.global.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User extends BaseTimeEntity {

  @JsonIgnore
  @Id
  @Column(name = "USER_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name = "EMAIL", length = 512, unique = true)
  private String email;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "NICK_NAME", length = 100)
  private String nickname;

  @Column(name = "PASSWORD", length = 128)
  private String password;

  @Column(name = "PHONE_NUMBER", length = 11)
  private String phoneNumber;

  @JsonIgnore
  @Column(name = "EMAIL_VERIFIED_YN", length = 1)
  private String emailVerifiedYn;

  @Column(name = "PROFILE_IMAGE_URL", length = 512)
  private String profileImageUrl;

  @Column(name = "ROLE_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private RoleType roles;

  @Column(name = "PROVIDER_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private ProviderType providerType;

  @Column(name = "ADDRESS", length = 512)
  private String address;

  @Column(name = "ADDRESS_DETAIL", length = 128)
  private String addressDetail;

  @Column(name = "SOCIAL_ID")
  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  @Column(name = "REFRESH_TOKEN", length = 512)
  private String refreshToken; // 리프레시 토큰

  @Builder
  public User(String name, String nickname, String password, String phoneNumber,
      String email, String address, String addressDetail,String profileImageUrl, ProviderType providerType) {
    this.name = name;
    this.nickname = nickname;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.addressDetail = addressDetail;
    this.profileImageUrl = profileImageUrl;
    this.socialId = "";
    this.providerType = ProviderType.LOCAL;
    this.roles = RoleType.USER;
  }

  public User(
      String name,
      String email,
      String emailVerifiedYn,
      String profileImageUrl,
      String socialId,
      String phoneNumber,
      ProviderType providerType,
      RoleType roles
  ) {
    this.name = name;
    this.password = "NO_PASS";
    this.email = email != null ? email : "NO_EMAIL";
    this.emailVerifiedYn = emailVerifiedYn;
    this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
    this.providerType = providerType;
    this.socialId = socialId;
    this.phoneNumber = phoneNumber;
    this.roles = roles;
  }

  //oauth2 이름 업데이트 되었을때
  public User update(String name) {
    this.name = name;
    return this;
  }

  public String getRoleKey() {
    return this.roles.getKey();
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

  //정보 수정용 setter
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setEmailVerifiedYn(String emailVerifiedYn) {
    this.emailVerifiedYn = emailVerifiedYn;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAddressDetail(String addressDetail) {
    this.addressDetail = addressDetail;
  }
}