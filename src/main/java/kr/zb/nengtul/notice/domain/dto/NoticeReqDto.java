package kr.zb.nengtul.notice.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.CONTENT_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.TITLE_NOT_NULL_MESSAGE;

import jakarta.validation.constraints.NotEmpty;
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

  @NotEmpty(message = TITLE_NOT_NULL_MESSAGE)
  private String title;
  @NotEmpty(message = CONTENT_NOT_NULL_MESSAGE)
  private String content;
  private String noticeImg;
}
