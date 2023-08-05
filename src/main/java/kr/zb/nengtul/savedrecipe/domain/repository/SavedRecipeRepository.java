package kr.zb.nengtul.savedrecipe.domain.repository;

import java.util.Optional;
import kr.zb.nengtul.savedrecipe.domain.entity.SavedRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {

  Optional<SavedRecipe> findByUserIdAndRecipeId(Long userId, String recipeId);

  Page<SavedRecipe> findAllByUserId(Long userId, Pageable pageable);

}
