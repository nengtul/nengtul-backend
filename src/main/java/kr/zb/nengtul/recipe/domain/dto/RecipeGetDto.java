package kr.zb.nengtul.recipe.domain.dto;

import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeGetDto {

    private String id;

    private Long userId;

    private String title;

    private String intro;

    private String ingredient;

    private String cookingStep;

    private String imageUrl;

    private String cookingTime;

    private String serving;

    private RecipeCategory category;

    private String videoUrl;

    private Long viewCount;

    public static RecipeGetDto fromRecipeDocument(RecipeDocument recipeDocument) {

        return RecipeGetDto.builder()
                .id(recipeDocument.getId())
                .userId(recipeDocument.getUserId())
                .title(recipeDocument.getTitle())
                .intro(recipeDocument.getIntro())
                .ingredient(recipeDocument.getIngredient())
                .cookingStep(recipeDocument.getCookingStep())
                .imageUrl(recipeDocument.getImageUrl())
                .cookingTime(recipeDocument.getCookingTime())
                .serving(recipeDocument.getServing())
                .category(recipeDocument.getCategory())
                .videoUrl(recipeDocument.getVideoUrl())
                .build();

    }

}
