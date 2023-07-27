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
public class ShareBoardListDto {

  private Long id; //게시물 id
  private Long userId; //채팅걸기위한 userId
  private String userNickname; //닉네임
  private String title; //제목
  private String shareImg; //대표사진
  private Long price; //가격
  private double lat; //위도
  private double lon; //경도
  private boolean isClosed; //거래되었는지(기본 false)
  private LocalDateTime createdAt; //작성시간
  private LocalDateTime modifiedAt;//수정시간

  public static ShareBoardListDto buildShareBoardListDto(ShareBoard shareBoard) {
    return ShareBoardListDto.builder()
        .id(shareBoard.getId())
        .userId(shareBoard.getUser().getId())
        .userNickname(shareBoard.getUser().getNickname())
        .title(shareBoard.getTitle())
        .shareImg(shareBoard.getShareImg())
        .price(shareBoard.getPrice())
        .lat(shareBoard.getLat())
        .lon(shareBoard.getLon())
        .isClosed(shareBoard.isClosed())
        .createdAt(shareBoard.getCreatedAt())
        .modifiedAt(shareBoard.getModifiedAt())
        .build();
  }
}
