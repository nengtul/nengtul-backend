package kr.zb.nengtul.user.domain.constants;

import lombok.Getter;

@Getter
public enum UserPoint {

  LIKES(1),
  FAVORITE(3);

  final int point;

  UserPoint(int point) {
    this.point = point;
  }
}
