package kr.zb.nengtul.comment.replycomment.domain.dto;

import java.time.LocalDateTime;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
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
public class ReplyCommentGetDto {

  private Long replyCommentId;
  private Long commentId;
  private Long userId;
  private String profileImageUrl;
  private int point;
  private String replyComment;
  private String userNickname;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static ReplyCommentGetDto buildCommentGetDto(ReplyComment replyComment) {
    return ReplyCommentGetDto.builder()
        .replyCommentId(replyComment.getId())
        .commentId(replyComment.getComment().getId())
        .userId(replyComment.getUser().getId())
        .profileImageUrl(replyComment.getUser().getProfileImageUrl())
        .point(replyComment.getUser().getPoint())
        .replyComment(replyComment.getReplyComment())
        .userNickname(replyComment.getUser().getNickname())
        .createdAt(replyComment.getCreatedAt())
        .modifiedAt(replyComment.getModifiedAt())
        .build();
  }
}
