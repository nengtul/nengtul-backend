package kr.zb.nengtul.global.oauth2;

import java.util.Collection;
import java.util.Map;
import kr.zb.nengtul.global.entity.RoleType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

  private final String email;
  private final RoleType role;

  public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes, String nameAttributeKey,
      String email, RoleType role) {
    super(authorities, attributes, nameAttributeKey);
    this.email = email;
    this.role = role;
  }
}