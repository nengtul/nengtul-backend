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

