package kr.zb.nengtul.savedrecipe.controller;

import java.security.Principal;
import kr.zb.nengtul.savedrecipe.domain.dto.SavedRecipeDto;
import kr.zb.nengtul.savedrecipe.service.SavedRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/saved-recipe")
public class SavedRecipeController {

  private final SavedRecipeService savedRecipeService;

  @PostMapping("/recipe/{recipeId}")
  ResponseEntity<Void> addSavedRecipe(Principal principal,
      @PathVariable String recipeId) {

    savedRecipeService.addSavedRecipe(principal, recipeId);

    return ResponseEntity.ok(null);
  }

  @GetMapping
  ResponseEntity<Page<SavedRecipeDto>> getSavedRecipe(Principal principal, Pageable pageable) {

    return ResponseEntity.ok(savedRecipeService.getSavedRecipe(principal, pageable));
  }

  @DeleteMapping("/{savedRecipeId}")
  ResponseEntity<Void> deleteSavedRecipe(Principal principal,
      @PathVariable Long savedRecipeId) {
    savedRecipeService.deleteSavedRecipe(principal, savedRecipeId);

    return ResponseEntity.ok(null);
  }

}
