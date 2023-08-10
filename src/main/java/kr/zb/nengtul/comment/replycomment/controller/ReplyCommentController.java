package kr.zb.nengtul.comment.replycomment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentReqDto;
import kr.zb.nengtul.comment.replycomment.service.ReplyCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "REPLY COMMENT API", description = "대댓글 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/comments/{commentId}")
public class ReplyCommentController {

  private final ReplyCommentService replyCommentService;

  @Operation(summary = "대댓글 작성", description = "토큰을 통해 회원 정보를 얻은 후 댓글에 대한 대댓글을 작성합니다.")
  @PostMapping
  public ResponseEntity<Void> createReplyComment(@PathVariable Long commentId,
      @RequestBody @Valid ReplyCommentReqDto replyCommentReqDto, Principal principal) {
    replyCommentService.createReplyComment(commentId, replyCommentReqDto, principal);
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "대댓글 수정", description = "토큰을 통해 회원 정보를 얻은 후 자신이 작성한 대댓글을 수정한다.")
  @PutMapping("/replycommets/{replyCommentId}")
  public ResponseEntity<Void> updateReplyComment(
      @Parameter(name = "commentId", description = "댓글 ID") @PathVariable Long commentId,
      @Parameter(name = "replyCommentId", description = "대댓글 ID") @PathVariable Long replyCommentId,
      @RequestBody @Valid ReplyCommentReqDto replyCommentReqDto,
      Principal principal) {
    replyCommentService.updateReplyComment(commentId, replyCommentId, replyCommentReqDto,
        principal);
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "대댓글 삭제", description = "토큰을 통해 회원 정보를 얻은 후 자신이 작성한 대댓글을 삭제한다.")
  @DeleteMapping("/replycommets/{replyCommentId}")
  public ResponseEntity<Void> deleteReplyComment(
      @Parameter(name = "commentId", description = "댓글 ID") @PathVariable Long commentId,
      @Parameter(name = "replyCommentId", description = "대댓글 ID") @PathVariable Long replyCommentId,
      Principal principal) {
    replyCommentService.deleteReplyComment(commentId, replyCommentId, principal);
    return ResponseEntity.ok(null);
  }
}
