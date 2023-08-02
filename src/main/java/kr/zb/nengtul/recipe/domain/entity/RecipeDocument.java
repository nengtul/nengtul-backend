package kr.zb.nengtul.recipe.domain.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.dto.RecipeUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "recipe")
@Setting(settingPath = "elasticsearch/elasticsearch-settings.json")
@Mapping(mappingPath = "elasticsearch/recipe-mappings.json")
public class RecipeDocument {

    @Id
    @Field(type = FieldType.Keyword)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field(type = FieldType.Keyword)
    private Long userId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword, index = false)
    private String intro;

    @Field(type = FieldType.Text)
    private String ingredient;

    @Field(type = FieldType.Keyword, index = false)
    private String cookingStep;

    @Field(type = FieldType.Keyword, index = false)
    private String thumbnailUrl;

    @Field(type = FieldType.Keyword, index = false)
    private String imageUrl;

    @Field(type = FieldType.Keyword)
    private String cookingTime;

    @Field(type = FieldType.Keyword)
    private String serving;

    @Field(type = FieldType.Keyword)
    private RecipeCategory category;

    @Field(type = FieldType.Keyword, index = false)
    private String videoUrl;

    @Field(type = FieldType.Integer)
    private Long viewCount;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private LocalDateTime modifiedAt;

    public void updateRecipe(RecipeUpdateDto recipeUpdateDto) {

        if (recipeUpdateDto.getTitle() != null) {
            this.title = recipeUpdateDto.getTitle();
        }

        if (recipeUpdateDto.getIntro() != null) {
            this.ingredient = recipeUpdateDto.getIntro();
        }

        if (recipeUpdateDto.getIngredient() != null) {
            this.ingredient = recipeUpdateDto.getIngredient();
        }

        if (recipeUpdateDto.getCookingStep() != null) {
            this.cookingStep = recipeUpdateDto.getCookingStep();
        }

        if (recipeUpdateDto.getImageUrl() != null) {
            this.imageUrl = recipeUpdateDto.getImageUrl();
        }

        if (recipeUpdateDto.getCookingTime() != null) {
            this.cookingTime = recipeUpdateDto.getCookingTime();
        }

        if (recipeUpdateDto.getServing() != null) {
            this.serving = recipeUpdateDto.getServing();
        }

        if (recipeUpdateDto.getRecipeCategory() != null) {
            this.category = recipeUpdateDto.getRecipeCategory();
        }

        if (recipeUpdateDto.getVideoUrl() != null) {
            this.videoUrl = recipeUpdateDto.getVideoUrl();
        }

        this.modifiedAt = LocalDateTime.now();

    }

}
