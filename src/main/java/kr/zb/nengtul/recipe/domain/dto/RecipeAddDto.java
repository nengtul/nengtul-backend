package kr.zb.nengtul.recipe.domain.dto;

import jakarta.validation.constraints.NotNull;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static kr.zb.nengtul.global.exception.ErrorCode.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeAddDto {

    @NotNull(message = TITLE_NOT_NULL_MESSAGE)
    private String title;

    @NotNull(message = INTRO_NOT_NULL_MESSAGE)
    private String intro;

    @NotNull(message = INGREDIENT_NOT_NULL_MESSAGE)
    private String ingredient;

    @NotNull(message = COOKING_STEP_NOT_NULL_MESSAGE)
    private String cookingStep;

    @NotNull(message = COOKING_TIME_NOT_NULL_MESSAGE)
    private String cookingTime;

    @NotNull(message = SERVING_NOT_NULL_MESSAGE)
    private String serving;

    @NotNull(message = CATEGORY_NOT_NULL_MESSAGE)
    private RecipeCategory category;

    private String videoUrl;

}
