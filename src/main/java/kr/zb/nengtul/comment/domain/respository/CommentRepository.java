package kr.zb.nengtul.comment.domain.respository;

import kr.zb.nengtul.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
