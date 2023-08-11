package kr.zb.nengtul.savedrecipe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.savedrecipe.domain.dto.SavedRecipeDto;
import kr.zb.nengtul.savedrecipe.domain.entity.SavedRecipe;
import kr.zb.nengtul.savedrecipe.domain.repository.SavedRecipeRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@DisplayName("레피시 저장 테스트")
class SavedRecipeServiceTest {

  private SavedRecipeService savedRecipeService;

  private SavedRecipeRepository savedRecipeRepository;

  private UserRepository userRepository;

  private RecipeSearchRepository recipeSearchRepository;

  @BeforeEach
  void setUp() {
    savedRecipeRepository = mock(SavedRecipeRepository.class);
    userRepository = mock(UserRepository.class);
    recipeSearchRepository = mock(RecipeSearchRepository.class);

    savedRecipeService = new SavedRecipeService(
        savedRecipeRepository, userRepository, recipeSearchRepository);

  }

  @Test
  @DisplayName("레시피 저장 성공")
  void addSavedRecipe_SUCCESS() {
    //given
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(new RecipeDocument()));

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    savedRecipeService.addSavedRecipe(principal, "recipeId");

    //then
    verify(savedRecipeRepository, times(1))
        .save(any(SavedRecipe.class));
  }

  @Test
  @DisplayName("레시피 저장 실패 - 이미 저장된 레시피")
  void addSavedRecipe_FAIL_Already_Saved_Recipe() {
    //given
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(new RecipeDocument()));

    when(savedRecipeRepository.findByUserIdAndRecipeId(any(), any()))
        .thenReturn(Optional.of(new SavedRecipe()));

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    CustomException customException =
        Assertions.assertThrows(CustomException.class,
            () -> savedRecipeService.addSavedRecipe(principal, "recipeId"));

    //then
    assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_ADDED_SAVED_RECIPE);
  }

  @Test
  @DisplayName("레시피 저장 가져오기 성공")
  void getSavedRecipe_SUCCESS() {
    //given
    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id("recipeId")
        .title("testTitle")
        .thumbnailUrl("testThumbnailUrl")
        .build();

    SavedRecipe savedRecipe = SavedRecipe.builder()
        .id(1L)
        .recipeId(recipeDocument.getId())
        .createdAt(LocalDateTime.now())
        .build();

    List<SavedRecipe> savedRecipes = new ArrayList<>();
    savedRecipes.add(savedRecipe);

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(new User()));

    when(savedRecipeRepository.findAllByUserId(any(), any()))
        .thenReturn(new PageImpl<>(savedRecipes));

    when(recipeSearchRepository.findById(any()))
        .thenReturn(Optional.of(recipeDocument));

    when(userRepository.findById(any()))
        .thenReturn(Optional.of(new User()));

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    SavedRecipeDto savedRecipeDto =
        savedRecipeService.getSavedRecipe(principal, Pageable.unpaged())
            .getContent().get(0);

    //then
    assertEquals(savedRecipeDto.getTitle(), recipeDocument.getTitle());
    assertEquals(savedRecipeDto.getThumbnailUrl(), recipeDocument.getThumbnailUrl());
    assertEquals(savedRecipeDto.getRecipeId(), recipeDocument.getId());
  }

  @Test
  @DisplayName("레피시 저장 삭제 성공")
  void deleteSavedRecipe_SUCCESS() {
    //given
    User user = mock(User.class);

    SavedRecipe savedRecipe = SavedRecipe.builder().user(user).build();

    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.of(user));

    when(savedRecipeRepository.findById(any()))
        .thenReturn(Optional.of(savedRecipe));

    when(user.getId())
        .thenReturn(1L);

    Principal principal = new UsernamePasswordAuthenticationToken(
        "test@test.com", null);

    //when
    savedRecipeService.deleteSavedRecipe(principal, 1L);

    //then
    verify(savedRecipeRepository, times(1))
        .delete(any(SavedRecipe.class));
  }

}