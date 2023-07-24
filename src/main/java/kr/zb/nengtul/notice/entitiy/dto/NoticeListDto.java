package kr.zb.nengtul.notice.entitiy.dto;

import java.time.LocalDateTime;
import kr.zb.nengtul.notice.entitiy.domain.Notice;
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
public class NoticeListDto {
  private Long noticeId;
  private String userName;
  private String title;
  private String noticeImg;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static NoticeListDto buildNoticeListDto(Notice notice) {
    return NoticeListDto.builder()
        .noticeId(notice.getId())
        .userName(notice.getUser().getName())
        .title(notice.getTitle())
        .noticeImg(notice.getNoticeImg())
        .createdAt(notice.getCreatedAt())
        .modifiedAt(notice.getModifiedAt())
        .build();
  }
}
