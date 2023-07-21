package kr.zb.nengtul.crawling.board.repository;


import kr.zb.nengtul.crawling.board.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {

}
