package kr.zb.nengtul.global.oauth2.dto;

import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;
import kr.zb.nengtul.global.oauth2.info.GoogleOAuth2UserInfo;
import kr.zb.nengtul.global.oauth2.info.KakaoOAuth2UserInfo;
import kr.zb.nengtul.global.oauth2.info.NaverOAuth2UserInfo;
import kr.zb.nengtul.user.entity.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

  private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
  private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

  @Builder
  public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
    this.nameAttributeKey = nameAttributeKey;
    this.oauth2UserInfo = oauth2UserInfo;
  }

  /**
   * ProviderType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
   * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
   * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
   * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
   */
  public static OAuthAttributes of(ProviderType socialType,
      String userNameAttributeName, Map<String, Object> attributes) {

    if (socialType == ProviderType.NAVER) {
      return ofNaver(userNameAttributeName, attributes);
    }
    if (socialType == ProviderType.KAKAO) {
      return ofKakao(userNameAttributeName, attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
        .build();
  }

  public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
        .build();
  }

  public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
        .build();
  }

  /**
   * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
   * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
   * role은 USER로 설정
   */
  public User toEntity(ProviderType providerType, OAuth2UserInfo oauth2UserInfo) {
    return new User(
        oauth2UserInfo.getName(),
        oauth2UserInfo.getEmail(),
        oauth2UserInfo.getImageUrl(),
        oauth2UserInfo.getSocialId(),
        oauth2UserInfo.getPhoneNumber(),
        providerType,
        RoleType.USER);
  }
}

