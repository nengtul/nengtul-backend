package kr.zb.nengtul.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import kr.zb.nengtul.comment.domain.dto.CommentGetDto;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "COMMENT API", description = "댓글 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/recipes")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 작성", description = "토큰을 통해 회원 정보를 얻은 후 게시물에 대한 댓글을 작성합니다.")
  @PostMapping("/{recipeId}/comments")
  public ResponseEntity<Void> createComment(
      @Parameter(name = "recipeId", description = "레시피 ID") @PathVariable String recipeId,
      @RequestBody @Valid CommentReqDto commentReqDto, Principal principal) {
    commentService.createComment(recipeId, commentReqDto, principal);
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "댓글 수정", description = "토큰을 통해 회원 정보를 댓글의 작성자와 비교한 후 댓글을 수정합니다.")
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<Void> updateComment(
      @Parameter(name = "commentId", description = "댓글 ID") @PathVariable Long commentId,
      @RequestBody @Valid CommentReqDto commentReqDto, Principal principal) {
    commentService.updateComment(commentId, commentReqDto, principal);
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "댓글 삭제", description = "토큰을 통해 회원 정보를 댓글의 작성자와 비교한 후 댓글을 삭제합니다.")
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @Parameter(name = "commentId", description = "댓글 ID") @PathVariable Long commentId,
      Principal principal) {
    commentService.deleteComment(commentId, principal);
    return ResponseEntity.ok(null);
  }

  @Operation(summary = "댓글 조회", description = "레시피ID 를 통해 레시피ID에 대한 전체 댓글을 호출합니다.")
  @GetMapping("/{recipeId}/commentlist")
  public ResponseEntity<List<CommentGetDto>> getComment(
      @Parameter(name = "recipeId", description = "레시피 ID") @PathVariable String recipeId) {
    return ResponseEntity.ok(commentService.findAllCommentByRecipeId(recipeId));
  }
}
