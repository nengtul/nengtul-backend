package kr.zb.nengtul.savedrecipe.service;

import java.security.Principal;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.savedrecipe.domain.dto.SavedRecipeDto;
import kr.zb.nengtul.savedrecipe.domain.entity.SavedRecipe;
import kr.zb.nengtul.savedrecipe.domain.repository.SavedRecipeRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {

  private final SavedRecipeRepository savedRecipeRepository;

  private final UserRepository userRepository;

  private final RecipeSearchRepository recipeSearchRepository;

  public void addSavedRecipe(Principal principal, String recipeId) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));

    savedRecipeRepository.findByUserIdAndRecipeId(user.getId(), recipeId)
        .ifPresent(likes -> {
          throw new CustomException(ErrorCode.ALREADY_ADDED_SAVED_RECIPE);
        });

    savedRecipeRepository.save(SavedRecipe.builder()
        .recipeId(recipeId)
        .user(user)
        .build());
  }

  public Page<SavedRecipeDto> getSavedRecipe(Principal principal, Pageable pageable) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return savedRecipeRepository.findAllByUserId(user.getId(), pageable)
        .map(savedRecipe -> {
          RecipeDocument recipeDocument = recipeSearchRepository.findById(savedRecipe.getRecipeId())
              .orElseGet(() -> RecipeDocument.builder()
                  .title("삭제된 레시피 입니다.")
                  .id("")
                  .title("")
                  .thumbnailUrl("")
                  .build());

          User recipeUser = userRepository.findById(recipeDocument.getUserId())
              .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

          return SavedRecipeDto.builder()
              .id(savedRecipe.getId())
              .createdAt(savedRecipe.getCreatedAt())
              .recipeId(recipeDocument.getId())
              .title(recipeDocument.getTitle())
              .thumbnailUrl(recipeDocument.getThumbnailUrl())
              .recipeUserNickname(recipeUser.getNickname())
              .build();
        });

  }

  @Transactional
  public void deleteSavedRecipe(Principal principal, Long savedRecipeId) {

    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    SavedRecipe savedRecipe = savedRecipeRepository.findById(savedRecipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SAVED_RECIPE));

    if (!user.getId().equals(savedRecipe.getUser().getId())) {
      throw new CustomException(ErrorCode.NO_PERMISSION);
    }

    savedRecipeRepository.delete(savedRecipe);
  }
}
