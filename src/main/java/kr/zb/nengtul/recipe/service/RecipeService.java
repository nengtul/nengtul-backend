package kr.zb.nengtul.recipe.service;

import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.dto.AddRecipeDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeGetDto;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeSearchRepository recipeSearchRepository;

    private final UserRepository userRepository;

    public void addRecipe(String userEmail, AddRecipeDto addRecipeDto) {

        User user = userRepository.findByEmail(userEmail).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        recipeSearchRepository.save(RecipeDocument.builder()
                .userId(user.getId())
                .title(addRecipeDto.getTitle())
                .intro(addRecipeDto.getIntro())
                .ingredient(addRecipeDto.getIngredient())
                .cookingStep(addRecipeDto.getCookingStep())
                .imageUrl(addRecipeDto.getImageUrl())
                .cookingTime(addRecipeDto.getCookingTime())
                .serving(addRecipeDto.getServing())
                .category(addRecipeDto.getCategory())
                .videoUrl(addRecipeDto.getVideoUrl())
                .build());

    }

    public RecipeGetDto getRecipeById(String recipeId) {

        return RecipeGetDto.fromRecipeDocument(
                recipeSearchRepository.findById(recipeId).orElseThrow());

    }

}
