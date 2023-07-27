package kr.zb.nengtul.recipe.controller;

import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.dto.RecipeAddDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeUpdateDto;
import kr.zb.nengtul.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    ResponseEntity<?> addRecipe(Principal principal,
                                @RequestBody RecipeAddDto recipeAddDto) {

        recipeService.addRecipe(principal, recipeAddDto);

        return ResponseEntity.ok("레시피 등록이 완료되었습니다.");
    }

    @GetMapping
    ResponseEntity<?> getAllRecipe(Pageable pageable) {

        return ResponseEntity.ok(recipeService.getAllRecipe(pageable));
    }

    @GetMapping("/detail/{recipeId}")
    ResponseEntity<?> getRecipeById(@PathVariable String recipeId) {

        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @GetMapping("/category/{category}")
    ResponseEntity<?> getRecipeByCategory(@PathVariable RecipeCategory category,
                                          Pageable pageable) {

        return ResponseEntity.ok(recipeService.getRecipeByCategory(category, pageable));
    }

    @GetMapping("/title/{title}")
    ResponseEntity<?> getRecipeByTitle(@PathVariable String title,
                                       Pageable pageable) {

        return ResponseEntity.ok(recipeService.getRecipeByTitle(title, pageable));
    }

    @GetMapping("/ingredient/{ingredient}")
    ResponseEntity<?> getRecipeByIngredient(@PathVariable String ingredient,
                                       Pageable pageable) {

        return ResponseEntity.ok(recipeService.getRecipeByIngredient(ingredient, pageable));
    }

    @PutMapping("/{recipeId}")
    ResponseEntity<?> updateRecipe(Principal principal,
                                   @PathVariable String recipeId,
                                   @RequestBody RecipeUpdateDto recipeUpdateDto) {

        recipeService.updateRecipe(principal, recipeId, recipeUpdateDto);

        return ResponseEntity.ok("레시피 업데이트가 완료됐습니다.");
    }

    @DeleteMapping("/{recipeId}")
    ResponseEntity<?> deleteRecipe(Principal principal, @PathVariable String recipeId) {

        recipeService.deleteRecipe(principal, recipeId);

        return ResponseEntity.ok("레시피 삭제가 완료됐습니다.");
    }

}
