package kr.zb.nengtul.recipe.domain.dto;

import jakarta.validation.constraints.NotNull;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeAddDto {

    @NotNull(message = "타이틀을 기재해 주세요.")
    private String title;

    @NotNull(message = "레시피 소개를 기재해 주세요.")
    private String intro;

    @NotNull(message = "재료를 기재해 주세요.")
    private String ingredient;

    @NotNull(message = "조리 순서를 기재해 주세요.")
    private String cookingStep;

    private String imageUrl;

    @NotNull(message = "조리 시간을 기재해 주세요.")
    private String cookingTime;

    @NotNull(message = "몇 인분인지 기재해 주세요.")
    private String serving;

    @NotNull(message = "카테고리를 기재해 주세요.")
    private RecipeCategory category;

    private String videoUrl;

}
