package kr.zb.nengtul.recipe.domain.dto;

import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushRecipeDto {

    private String recipeId;
    private String title;
    private String publisherNickname;

    public static PushRecipeDto fromEntity(RecipeDocument recipe, User user){
        return PushRecipeDto.builder()
                .recipeId(recipe.getId())
                .title(recipe.getTitle())
                .publisherNickname(user.getNickname())
                .build();
    }
}
