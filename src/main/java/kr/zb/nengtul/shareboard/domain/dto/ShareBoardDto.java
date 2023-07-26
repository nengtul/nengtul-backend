package kr.zb.nengtul.shareboard.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.CONTENT_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LAT_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LON_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.TITLE_NOT_NULL_MESSAGE;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ShareBoardDto {

  @NotEmpty(message = TITLE_NOT_NULL_MESSAGE)
  private String title;

  @NotEmpty(message = CONTENT_NOT_NULL_MESSAGE)
  private String content;

  private String shareImg;

  private Long price;

  @NotNull(message = LAT_NOT_NULL_MESSAGE)
  private double lat;

  @NotNull(message = LON_NOT_NULL_MESSAGE)
  private double lon;
}
