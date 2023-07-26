package kr.zb.nengtul.shareboard.domain.dto;

import static kr.zb.nengtul.global.exception.ErrorCode.CONTENT_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LAT_AVERAGE_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LAT_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LON_AVERAGE_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.LON_NOT_NULL_MESSAGE;
import static kr.zb.nengtul.global.exception.ErrorCode.TITLE_NOT_NULL_MESSAGE;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

  private String shareImg;

  private Long price;

  @NotNull(message = LAT_NOT_NULL_MESSAGE)
  @DecimalMin(value = "-90.0", message = LAT_AVERAGE_MESSAGE)
  @DecimalMax(value = "90.0", message = LAT_AVERAGE_MESSAGE)
  private Double lat;

  @NotNull(message = LON_NOT_NULL_MESSAGE)
  @DecimalMin(value = "-180.0", message = LON_AVERAGE_MESSAGE)
  @DecimalMax(value = "180.0", message = LON_AVERAGE_MESSAGE)
  private Double lon;
}
