package kr.zb.nengtul.comment.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentGetDto;
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
  private String comment;
  private String profileImageUrl;
  private int point;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private List<ReplyCommentGetDto> replyCommentGetDtoList;
}
