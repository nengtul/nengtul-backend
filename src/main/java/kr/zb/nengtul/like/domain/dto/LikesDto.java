package kr.zb.nengtul.like.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikesDto {

  private Long id;

  private String recipeId;

  private String title;

  private String thumbnailUrl;

  private LocalDateTime createdAt;

}
