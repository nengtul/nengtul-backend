package kr.zb.nengtul.recipe.controller;

import kr.zb.nengtul.global.jwt.service.JwtUserDetails;
import kr.zb.nengtul.recipe.domain.dto.AddRecipeDto;
import kr.zb.nengtul.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    ResponseEntity<?> addRecipe(@AuthenticationPrincipal JwtUserDetails user,
            @RequestBody AddRecipeDto addRecipeDto) {

        recipeService.addRecipe(user.getUsername(), addRecipeDto);

        return ResponseEntity.ok("레시피 등록이 완료되었습니다.");
    }

    @GetMapping("{recipeId}")
    ResponseEntity<?> getRecipe(@PathVariable String recipeId){

        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }


}
