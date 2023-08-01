package kr.zb.nengtul.comment.domain.dto;

import java.time.LocalDateTime;
import kr.zb.nengtul.comment.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentGetDto {

  private String recipeId;
  private Long commentId;
  private Long userId;
  private String userNickname;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static CommentGetDto buildCommentGetDto(Comment comment) {
    return CommentGetDto.builder()
        .recipeId(comment.getRecipeId())
        .commentId(comment.getId())
        .userId(comment.getUser().getId())
        .userNickname(comment.getUser().getNickname())
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .modifiedAt(comment.getModifiedAt())
        .build();
  }
}
