package kr.zb.nengtul.global.oauth2.info;

import java.util.Map;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getEmail() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    return (String) kakaoAccount.get("email");
  }

  @Override
  public String getName() {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    if (account == null || profile == null) {
      return null;
    }

    return (String) profile.get("nickname");
  }

  @Override
  public String getSocialId() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }
    return (String) response.get("id");
  }

  @Override
  public String getPhoneNumber() {
    return "";
  }

  @Override
  public String getImageUrl() {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    if (account == null || profile == null) {
      return null;
    }

    return (String) profile.get("thumbnail_image_url");
  }
}