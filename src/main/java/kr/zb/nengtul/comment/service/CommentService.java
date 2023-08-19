package kr.zb.nengtul.comment.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_COMMENT;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import kr.zb.nengtul.comment.domain.dto.CommentGetDto;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.comment.replycomment.service.ReplyCommentService;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

  private final RecipeSearchRepository recipeSearchRepository;
  private final CommentRepository commentRepository;
  private final UserService userService;
  private final ReplyCommentService replyCommentService;

  @Transactional
  public void createComment(String recipeId, CommentReqDto commentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));
    commentRepository.save(Comment.builder()
        .recipeId(recipeDocument.getId())
        .user(user)
        .comment(commentReqDto.getComment())
        .build());
  }

  @Transactional
  public void updateComment(Long commentId, CommentReqDto commentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = commentRepository.findByIdAndUser(commentId, user)
        .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
    comment.setComment(commentReqDto.getComment());
    commentRepository.save(comment);
  }

  @Transactional
  public void deleteComment(Long commentId, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
    if (user.getRoles().equals(RoleType.ADMIN) || user.equals(comment.getUser())) {
      commentRepository.delete(comment);
    } else {
      throw new CustomException(NO_PERMISSION);
    }
  }

  public List<CommentGetDto> findAllCommentByRecipeId(String recipeId) {
    RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RECIPE));
    List<Comment> commentList = commentRepository.findAllByRecipeId(recipeDocument.getId());

    return commentList.stream().map(this::buildCommentGetDto).collect(Collectors.toList());
  }

  public CommentGetDto buildCommentGetDto(Comment comment) {
    return CommentGetDto.builder()
        .recipeId(comment.getRecipeId())
        .commentId(comment.getId())
        .userId(comment.getUser().getId())
        .userNickname(comment.getUser().getNickname())
        .profileImageUrl(comment.getUser().getProfileImageUrl())
        .point(comment.getUser().getPoint())
        .comment(comment.getComment())
        .createdAt(comment.getCreatedAt())
        .modifiedAt(comment.getModifiedAt())
        .replyCommentGetDtoList(
            replyCommentService.getReplyCommentByComment(comment.getId()))
        .build();
  }
}
