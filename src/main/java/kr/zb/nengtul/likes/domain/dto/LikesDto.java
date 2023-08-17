package kr.zb.nengtul.likes.domain.dto;

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

  private String recipeUserNickName;

  private Long likeCount;

  private Long viewCount;

  private LocalDateTime createdAt;

}
