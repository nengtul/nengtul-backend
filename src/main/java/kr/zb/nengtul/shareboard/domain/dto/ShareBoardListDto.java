package kr.zb.nengtul.shareboard.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.LONG_CONTENT_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LONG_PLACE_MESSAGE;

import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import kr.zb.nengtul.global.exception.ErrorCode;
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
  private int point;
  private String title; //제목
  private String content; //글 내용
  private String place; //위치
  private String shareImg; //사진 목록
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
        .point(shareBoard.getUser().getPoint())
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
