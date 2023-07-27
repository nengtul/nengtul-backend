package kr.zb.nengtul.recipe.domain.dto;

import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecipeGetDetailDto {

    private String id;

    private Long userId;

    private String nickName;

    private String title;

    private String intro;

    private String ingredient;

    private String cookingStep;

    private String imageUrl;

    private String cookingTime;

    private String serving;

    private String category;

    private String videoUrl;

    private Long viewCount;

    public static RecipeGetDetailDto fromRecipeDocument(RecipeDocument recipeDocument) {

        return RecipeGetDetailDto.builder()
                .id(recipeDocument.getId())
                .userId(recipeDocument.getUserId())
                .title(recipeDocument.getTitle())
                .intro(recipeDocument.getIntro())
                .ingredient(recipeDocument.getIngredient())
                .cookingStep(recipeDocument.getCookingStep())
                .imageUrl(recipeDocument.getImageUrl())
                .cookingTime(recipeDocument.getCookingTime())
                .serving(recipeDocument.getServing())
                .category(recipeDocument.getCategory().getKorean())
                .videoUrl(recipeDocument.getVideoUrl())
                .build();

    }

}
