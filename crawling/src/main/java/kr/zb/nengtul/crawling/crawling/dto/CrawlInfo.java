package kr.zb.nengtul.crawling.crawling.dto;

import kr.zb.nengtul.crawling.recipe.type.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CrawlInfo {

    private RecipeCategory category;
    private String recipeUrl;
    private String mainPhotoUrl;

}
