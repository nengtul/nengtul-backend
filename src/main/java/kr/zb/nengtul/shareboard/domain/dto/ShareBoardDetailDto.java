package kr.zb.nengtul.shareboard.domain.dto;

import java.time.LocalDateTime;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
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
public class ShareBoardDetailDto {

  private Long id;
  private Long userId;
  private String userNickname;
  private String title;
  private String content;
  private Long price;
  private double lat;
  private double lon;
  private long views;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;


  public static ShareBoardDetailDto buildShareBoardDetailDto(ShareBoard shareBoard) {
    return ShareBoardDetailDto.builder()
        .id(shareBoard.getId())
        .userId(shareBoard.getUser().getId())
        .userNickname(shareBoard.getUser().getNickname())
        .title(shareBoard.getTitle())
        .content(shareBoard.getContent())
        .price(shareBoard.getPrice())
        .lat(shareBoard.getLat())
        .lon(shareBoard.getLon())
        .views(shareBoard.getViews())
        .createdAt(shareBoard.getCreatedAt())
        .modifiedAt(shareBoard.getModifiedAt())
        .build();
  }
}