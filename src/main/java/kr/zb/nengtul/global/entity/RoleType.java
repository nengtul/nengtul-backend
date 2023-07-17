package kr.zb.nengtul.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
  GUEST("ROLE_GUEST"),
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String key;

}
