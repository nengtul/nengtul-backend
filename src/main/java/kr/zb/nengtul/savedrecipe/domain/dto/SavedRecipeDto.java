package kr.zb.nengtul.savedrecipe.domain.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SavedRecipeDto {

  private Long id;

  private String recipeId;

  private String title;

  private String thumbnailUrl;

  private String recipeUserNickname;

  private LocalDateTime createdAt;

}
