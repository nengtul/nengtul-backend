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
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.oauth.entity.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Column(name = "PROVIDER_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private OAuthProvider oAuthProvider;

  @Column(name = "ROLE_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private RoleType roles;

  @Column(name = "ADDRESS", length = 512)
  private String address;

  @Column(name = "ADDRESS_DETAIL", length = 128)
  private String addressDetail;


  @Builder
  public User( String name, String nickname, String password, String phoneNumber,
      String email, String address, String addressDetail) {
//    this.loginId = loginId;
    this.name = name;
    this.nickname = nickname;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.addressDetail = addressDetail;
    this.roles = RoleType.USER;
  }

//  @Builder
//  public User(String nickname, String email,
//      OAuthProvider oAuthProvider) {
//    this.nickname = nickname;
//    this.email = email;
//    this.oAuthProvider = oAuthProvider;
//  }



//  // 프로필 사진
//  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//  private UploadFile profileImg;
}