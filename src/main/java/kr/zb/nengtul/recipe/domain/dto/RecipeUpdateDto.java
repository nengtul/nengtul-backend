package kr.zb.nengtul.recipe.domain.dto;

import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeUpdateDto {

    private String title;

    private String intro;

    private String ingredient;

    private String imagesUrl;

    private String cookingStep;

    private String cookingTime;

    private String serving;

    private RecipeCategory category;

    private String videoUrl;

}
