package kr.zb.nengtul.global.oauth2.info;

import java.util.Map;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

  public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

  @Override
  public String getSocialId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }

  @Override
  public String getPhoneNumber() {
    return "";
  }

  @Override
  public String getImageUrl() {
    return (String) attributes.get("picture");
  }
}