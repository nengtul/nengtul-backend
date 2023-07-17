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
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
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

  @Column(name = "SOCIAL_ID", length = 30)
  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  @Column(name = "REFRESH_TOKEN", length = 512)
  private String refreshToken; // 리프레시 토큰

  @Builder
  public User(String name, String nickname, String password, String phoneNumber,
      String email, String address, String addressDetail, ProviderType providerType) {
    this.name = name;
    this.nickname = nickname;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.addressDetail = addressDetail;
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

  public User update(String name) {
    this.name = name;
    return this;
  }

  public String getRoleKey() {
    return this.roles.getKey();
  }

  // 비밀번호 암호화 메소드
  public void passwordEncode(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }
}