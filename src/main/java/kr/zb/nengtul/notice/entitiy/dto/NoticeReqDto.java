package kr.zb.nengtul.notice.entitiy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReqDto {
  private String title;
  private String content;
  private String noticeImg;
}
