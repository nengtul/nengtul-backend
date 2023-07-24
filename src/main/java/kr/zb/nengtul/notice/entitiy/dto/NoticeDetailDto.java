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
public class NoticeDetailDto {
  private Long id;
  private Long userId;
  private String userName;
  private String title;
  private String content;
  private String noticeImg;
  private Long viewCount;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static NoticeDetailDto buildNoticeDetailDto(Notice notice) {
    return NoticeDetailDto.builder()
        .id(notice.getId())
        .userId(notice.getUser().getId())
        .userName(notice.getUser().getName())
        .title(notice.getTitle())
        .content(notice.getContent())
        .noticeImg(notice.getNoticeImg())
        .createdAt(notice.getCreatedAt())
        .modifiedAt(notice.getModifiedAt())
        .viewCount(notice.getViewCount())
        .build();
  }
}
