package kr.zb.nengtul.comment.replycomment.domain.repository;


import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<ReplyComment, Long> {

  List<ReplyComment> findByCommentId(Long commentId);
  Optional<ReplyComment> findByIdAndCommentId(Long id, Long commentId);

}
