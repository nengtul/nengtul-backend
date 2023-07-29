package kr.zb.nengtul.recipe.controller;

import jakarta.validation.Valid;
import kr.zb.nengtul.recipe.domain.constants.RecipeCategory;
import kr.zb.nengtul.recipe.domain.dto.RecipeAddDto;
import kr.zb.nengtul.recipe.domain.dto.RecipeUpdateDto;
import kr.zb.nengtul.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    ResponseEntity<?> addRecipe(Principal principal,
                                @RequestPart @Valid RecipeAddDto recipeAddDto,
                                @RequestPart List<MultipartFile> images) {

        recipeService.addRecipe(principal, recipeAddDto, images);

        return ResponseEntity.ok(null);
    }

    @GetMapping
    ResponseEntity<?> getAllRecipe(Pageable pageable) {

        return ResponseEntity.ok(recipeService.getAllRecipe(pageable));
    }

    @GetMapping("/detail/{recipeId}")
    ResponseEntity<?> getRecipeById(@PathVariable String recipeId) {

        return ResponseEntity.ok(recipeService.getRecipeDetailById(recipeId));
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

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{recipeId}")
    ResponseEntity<?> deleteRecipe(Principal principal, @PathVariable String recipeId) {

        recipeService.deleteRecipe(principal, recipeId);

        return ResponseEntity.ok(null);
    }

}
