package kr.zb.nengtul.likes.domain.repository;

import java.util.Optional;
import kr.zb.nengtul.likes.domain.entity.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

  Page<Likes> findAllByUserId(Long userId, Pageable pageable);

  Optional<Likes> findByUserIdAndRecipeId(Long id, String recipeId);

  Long countByRecipeId(String recipeId);
  int countByUserId(Long userId);
}
