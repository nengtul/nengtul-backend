package kr.zb.nengtul.user.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.likes.domain.entity.Likes;
import kr.zb.nengtul.notice.domain.entity.Notice;
import kr.zb.nengtul.savedrecipe.domain.entity.SavedRecipe;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.constants.UserPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String name;

  @Column(unique = true)
  private String nickname;

  private String password;

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

  @JsonBackReference
  @OneToMany(mappedBy = "user")
  private List<Notice> noticeList;

  @JsonBackReference
  @OneToMany(mappedBy = "user")
  private List<ShareBoard> shareBoardList;

  @JsonBackReference
  @OneToMany(mappedBy = "user")
  private List<ReplyComment> replyCommentList;

  @JsonBackReference
  @OneToMany(mappedBy = "user")
  private List<Comment> commentList;

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Likes> likesList;

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Favorite> favoriteList;

  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<SavedRecipe> savedRecipeList;

  @Builder
  public User(String name, String nickname, String password, String phoneNumber,
      String email, String address, String addressDetail, String profileImageUrl) {
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

  //test 코드용
  public User(Long id){
    this.id = id;

  }
  //OAuth2용
  public User(
      String name,
      String nickname,
      String email,
      String profileImageUrl,
      String socialId,
      String phoneNumber,
      ProviderType providerType,
      RoleType roles
  ) {
    this.name = name;
    this.nickname = nickname;
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

  public void setEmail(String email) {
    this.address = email;
  }


  public void setAddressDetail(String addressDetail) {
    this.addressDetail = addressDetail;
  }

  public void setRoles(RoleType roles) {
    this.roles = roles;
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

  public void setPlusPoint(UserPoint userPoint) {
    this.point += userPoint.getPoint();
  }

  public void setMinusPoint(UserPoint userPoint) {
    this.point -= userPoint.getPoint();
  }

  public boolean isEmailVerifiedYn() {
    return emailVerifiedYn;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNoticeList(List<Notice> noticeList) {
    this.noticeList = noticeList;
  }

  public void setShareBoardList(
      List<ShareBoard> shareBoardList) {
    this.shareBoardList = shareBoardList;
  }

  public void setReplyCommentList(
      List<ReplyComment> replyCommentList) {
    this.replyCommentList = replyCommentList;
  }

  public void setCommentList(List<Comment> commentList) {
    this.commentList = commentList;
  }

  public void setLikesList(List<Likes> likesList) {
    this.likesList = likesList;
  }

  public void setFavoriteList(List<Favorite> favoriteList) {
    this.favoriteList = favoriteList;
  }

  public void setSavedRecipeList(
      List<SavedRecipe> savedRecipeList) {
    this.savedRecipeList = savedRecipeList;
  }
}