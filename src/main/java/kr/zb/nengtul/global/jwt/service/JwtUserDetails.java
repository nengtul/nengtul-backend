package kr.zb.nengtul.global.jwt.service;

import java.util.ArrayList;
import java.util.Collection;
import kr.zb.nengtul.user.entity.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

  private final User user;

  public JwtUserDetails(User user) {
    this.user = user;
  }

  @Override

  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoles().getKey()));

    return authorities;

  }

  //principle.getName() 하면 email 반환
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }


  // == 세부 설정 == //

  @Override
  public boolean isAccountNonExpired() { // 계정의 만료 여부
    return true;
  }

  @Override
  public boolean isAccountNonLocked() { // 계정의 잠김 여부
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() { // 비밀번호 만료 여부
    return true;
  }

  @Override
  public boolean isEnabled() { // 계정의 활성화 여부
    return true;
  }
}
