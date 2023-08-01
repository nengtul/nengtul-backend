package kr.zb.nengtul.comment.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import kr.zb.nengtul.comment.domain.dto.CommentGetDto;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

  private final RecipeSearchRepository recipeSearchRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;

  @Transactional
  public void createComment(String recipeId, CommentReqDto commentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));
    Comment comment = Comment.builder()
        .recipeId(recipeDocument.getId())
        .user(user)
        .content(commentReqDto.getContent())
        .build();
    commentRepository.save(comment);
  }

  @Transactional
  public void updateComment(Long commentId, CommentReqDto commentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = commentRepository.findByIdAndUser(commentId, user)
        .orElseThrow(() -> new CustomException(NO_PERMISSION));
    comment.setContent(commentReqDto.getContent());
    commentRepository.save(comment);
  }
  @Transactional
  public void deleteComment(Long commentId, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = commentRepository.findByIdAndUser(commentId, user)
        .orElseThrow(() -> new CustomException(NO_PERMISSION));
    commentRepository.delete(comment);
  }

  public Page<CommentGetDto> findAllCommentByRecipeId(String recipeId, Pageable pageable){
    Page<Comment> commentList = commentRepository.findAllByRecipeId(recipeId, pageable);
    return commentList.map(CommentGetDto::buildCommentGetDto);
  }
}
