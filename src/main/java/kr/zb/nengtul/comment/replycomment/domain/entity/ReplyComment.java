package kr.zb.nengtul.comment.replycomment.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ReplyComment extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonManagedReference
  @ManyToOne
  private User user;

  @JsonManagedReference
  @ManyToOne
  private Comment comment;

  private String replyComment;

  public void setReplyComment(String replyComment) {
    this.replyComment = replyComment;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
