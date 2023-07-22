package kr.zb.nengtul.user.entity.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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
public class User extends BaseTimeEntity {

  @JsonIgnore
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 512, unique = true)
  private String email;

  @Column(length = 10)
  private String name;

  @Column(length = 10)
  private String nickname;

  @Column(length = 100) //password encoder에서 길어지기 때문에 길이 증가
  private String password;

  @Column(length = 11)
  private String phoneNumber;

  private LocalDateTime verifyExpiredAt;
  private String verificationCode;
  private boolean emailVerifiedYn;

  private String profileImageUrl;
  
  private int point; // 포인트별 등급 상승을 위해 생성

  @Enumerated(EnumType.STRING)
  private RoleType roles;

  @Enumerated(EnumType.STRING)
  private ProviderType providerType;

  private String address;
  private String addressDetail;

  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  private String refreshToken; // 리프레시 토큰

  @Builder
  public User(String name, String nickname, String password, String phoneNumber,
      String email, String address, String addressDetail,String profileImageUrl) {
    this.name = name;
    this.nickname = nickname;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.emailVerifiedYn = false;
    this.address = address;
    this.addressDetail = addressDetail;
    this.profileImageUrl = profileImageUrl;
    this.socialId = "";
    this.providerType = ProviderType.LOCAL;
    this.roles = RoleType.USER;
  }

  //OAuth2용
  public User(
      String name,
      String email,
      String profileImageUrl,
      String socialId,
      String phoneNumber,
      ProviderType providerType,
      RoleType roles
  ) {
    this.name = name;
    this.password = "NO_PASS";
    this.email = email != null ? email : "NO_EMAIL";
    this.verificationCode = "";
    this.verifyExpiredAt = LocalDateTime.now();
    this.emailVerifiedYn = true;
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



  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setAddressDetail(String addressDetail) {
    this.addressDetail = addressDetail;
  }

  public void setEmailVerifiedYn(boolean emailVerifiedYn) {
    this.emailVerifiedYn = emailVerifiedYn;
  }
  public void setVerifyExpiredAt(LocalDateTime verifyExpiredAt) {
    this.verifyExpiredAt = verifyExpiredAt;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
}