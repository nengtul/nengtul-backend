package kr.zb.nengtul.recipe.domain.dto;

import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeUpdateDto {

    private String title;

    private String intro;

    private String ingredient;

    private String cookingStep;

    private String imageUrl;

    private String cookingTime;

    private String serving;

    private RecipeCategory recipeCategory;

    private String videoUrl;

}
