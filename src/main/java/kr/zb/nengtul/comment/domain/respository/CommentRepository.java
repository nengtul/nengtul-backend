package kr.zb.nengtul.comment.domain.respository;

import java.util.Optional;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Optional<Comment> findByIdAndUser(Long id, User user);
  Page<Comment> findAllByRecipeId(String recipeId, Pageable pageable);
}
