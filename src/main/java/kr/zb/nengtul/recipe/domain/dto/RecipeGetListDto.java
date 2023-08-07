package kr.zb.nengtul.recipe.domain.dto;


import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import lombok.*;

@Builder
@Setter
@Getter
public class RecipeGetListDto {

    private String recipeId;

    private String title;

    private String nickName;

    private Long viewCount;

    private Long likeCount;

    private String thumbnailUrl;

    public static RecipeGetListDto fromRecipeDocument(RecipeDocument recipeDocument) {

        return RecipeGetListDto.builder()
                .recipeId(recipeDocument.getId())
                .title(recipeDocument.getTitle())
                .viewCount(recipeDocument.getViewCount())
                .thumbnailUrl(recipeDocument.getThumbnailUrl())
                .build();

    }

}
