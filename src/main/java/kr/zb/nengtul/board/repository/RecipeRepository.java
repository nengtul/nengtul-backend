package kr.zb.nengtul.board.repository;

import kr.zb.nengtul.board.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

}
