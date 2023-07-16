package kr.zb.nengtul.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
  USER("USER", "유저"),
  ADMIN("ADMIN", "관리자");

  private final String key;
  private final String title;

}
