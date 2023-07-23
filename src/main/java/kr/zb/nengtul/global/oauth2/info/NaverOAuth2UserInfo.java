package kr.zb.nengtul.global.oauth2.info;

import java.util.Map;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

  public NaverOAuth2UserInfo(Map<String, Object> attributes) {
    super((Map<String, Object>) attributes.get("response"));
  }

  @Override
  public String getSocialId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }
  @Override
  public String getPhoneNumber() {
    return String.valueOf(attributes.get("mobile")).replaceAll("-","");
  }
  @Override
  public String getImageUrl() {
    return (String) attributes.get("profile_image");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }
}