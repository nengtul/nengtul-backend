package kr.zb.nengtul.comment.domain.respository;

import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Optional<Comment> findByIdAndUser(Long id, User user);
  List<Comment> findAllByRecipeId(String recipeId);
}
