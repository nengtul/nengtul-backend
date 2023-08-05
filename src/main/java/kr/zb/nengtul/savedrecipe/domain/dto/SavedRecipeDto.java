package kr.zb.nengtul.savedrecipe.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class SavedRecipeDto {

  private Long id;

  private String recipeId;

  private String title;

  private String thumbnailUrl;

  private LocalDateTime createdAt;

}
