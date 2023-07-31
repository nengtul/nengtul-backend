package kr.zb.nengtul.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "COMMENT API", description = "댓글 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/recipe/{recipeId}")
public class CommentController {
  private final CommentService commentService;
  @Operation(summary = "댓글 작성", description = "토큰을 통해 회원 정보를 얻은 후 게시물에 대한 댓글을 작성합니다.")
  @PostMapping
  public ResponseEntity<Void> createComment(@PathVariable Long recipeId,
      @RequestBody @Valid CommentReqDto commentReqDto,
      Principal principal) {
    commentService.createComment(recipeId, commentReqDto, principal);
    return ResponseEntity.ok(null);
  }
}
