package kr.zb.nengtul.comment.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
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
public class Comment extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String recipeId;

  @JsonManagedReference
  @ManyToOne
  private User user;

  private String comment;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<ReplyComment> replyCommentList;

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
