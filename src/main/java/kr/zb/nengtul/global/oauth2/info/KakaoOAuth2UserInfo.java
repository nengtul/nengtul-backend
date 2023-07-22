package kr.zb.nengtul.global.oauth2.info;

import java.util.Map;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getEmail() {
    return (String) getValue("kakao_account", "email");
  }

  @Override
  public String getName() {
    return (String) getValue("kakao_account", "profile", "nickname");
  }

  @Override
  public String getSocialId() {
    return (String) getValue("response", "id");
  }

  @Override
  public String getPhoneNumber() {
    return "";
  }

  @Override
  public String getImageUrl() {
    return (String) getValue("kakao_account", "profile", "thumbnail_image_url");
  }

  private Object getValue(String... keys) {
    Map<String, Object> current = attributes;
    for (String key : keys) {
      current = (Map<String, Object>) current.get(key);
      if (current == null) {
        return null;
      }
    }
    return current;
  }
}
