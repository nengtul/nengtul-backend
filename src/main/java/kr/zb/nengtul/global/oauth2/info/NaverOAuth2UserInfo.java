package kr.zb.nengtul.global.oauth2.info;

import java.util.Map;
import kr.zb.nengtul.global.oauth2.OAuth2UserInfo;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

  public NaverOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
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
  public String getName() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }

    return (String) response.get("name");
  }
  @Override
  public String getPhoneNumber() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if(response == null) {
      return "";
    }
    return String.valueOf(response.get("mobile")).replaceAll("-","");
  }
  @Override
  public String getImageUrl() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }

    return (String) response.get("profile_image");
  }

  @Override
  public String getEmail() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }

    return (String) response.get("email");
  }
}