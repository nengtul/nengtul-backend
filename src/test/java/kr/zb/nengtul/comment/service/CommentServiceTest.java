package kr.zb.nengtul.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.comment.domain.dto.CommentGetDto;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentGetDto;
import kr.zb.nengtul.comment.replycomment.service.ReplyCommentService;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("댓글 테스트")
class CommentServiceTest {

  private CommentService commentService;
  private RecipeSearchRepository recipeSearchRepository;
  private CommentRepository commentRepository;
  private UserService userService;
  private ReplyCommentService replyCommentService;

  @BeforeEach
  void setUp() {
    commentService = mock(CommentService.class);
    recipeSearchRepository = mock(RecipeSearchRepository.class);
    commentRepository = mock(CommentRepository.class);
    userService = mock(UserService.class);
    replyCommentService = mock(ReplyCommentService.class);

    commentService = new CommentService(
        recipeSearchRepository, commentRepository, userService, replyCommentService);
  }


  @Test
  @DisplayName("댓글 작성 성공")
  void createComment_SUCCESS() {
    // given
    String recipeId = "test_recipe_id";
    String userEmail = "test@example.com";
    CommentReqDto commentReqDto = new CommentReqDto();
    commentReqDto.setComment("테스트 댓글");

    User user = User.builder()
        .email(userEmail)
        .build();
    RecipeDocument recipeDocument = RecipeDocument.builder()
        .id(recipeId)
        .build();

    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(recipeSearchRepository.findById(recipeId)).thenReturn(Optional.of(recipeDocument));

    Comment comment = Comment.builder()
        .recipeId(recipeId)
        .user(user)
        .comment(commentReqDto.getComment())
        .build();

    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    // when
    commentService.createComment(recipeId, commentReqDto, mockPrincipal(userEmail));

    // then
    ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
    verify(commentRepository).save(commentCaptor.capture());

    Comment capturedComment = commentCaptor.getValue();
    assertEquals(recipeId, capturedComment.getRecipeId());
    assertEquals(user, capturedComment.getUser());
    assertEquals(commentReqDto.getComment(), capturedComment.getComment());
  }


  @Test
  @DisplayName("댓글 수정 성공")
  void updateComment_SUCCESS() {
    // given
    Long commentId = 1L;
    String userEmail = "test@example.com";
    CommentReqDto commentReqDto = new CommentReqDto();
    commentReqDto.setComment("업데이트된 댓글");

    User user = User.builder()
        .email(userEmail)
        .build();

    Comment existingComment = Comment.builder()
        .id(commentId)
        .recipeId("test_recipe_id")
        .user(user)
        .comment("기존 댓글")
        .build();

    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(commentRepository.findByIdAndUser(commentId, user)).thenReturn(Optional.of(existingComment));

    // when
    commentService.updateComment(commentId, commentReqDto, mockPrincipal(userEmail));

    // then
    ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
    verify(commentRepository).save(commentCaptor.capture());

    Comment updatedComment = commentCaptor.getValue();
    assertEquals(commentId, updatedComment.getId());
    assertEquals(existingComment.getRecipeId(), updatedComment.getRecipeId());
    assertEquals(user, updatedComment.getUser());
    assertEquals(commentReqDto.getComment(), updatedComment.getComment());
  }

  @Test
  @DisplayName("댓글 삭제 성공")
  void deleteComment_SUCCESS() {
    // given
    Long commentId = 1L;
    String userEmail = "test@example.com";

    User user = User.builder()
        .email(userEmail)
        .build();

    Comment existingComment = Comment.builder()
        .id(commentId)
        .recipeId("test_recipe_id")
        .user(user)
        .comment("삭제될 댓글")
        .build();

    // Mock 설정 (Mock setup)
    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
    doNothing().when(commentRepository).delete(existingComment);

    // when
    commentService.deleteComment(commentId, mockPrincipal(userEmail));

    // then
    verify(commentRepository).delete(existingComment);
  }

  @Test
  @DisplayName("댓글 조회 성공")
  void findAllCommentByRecipeId_SUCCESS() {
    // given
    String recipeId = "test_recipe_id";

    User user = User.builder()
        .nickname("test_user")
        .build();

    List<Comment> commentList = Arrays.asList(
        Comment.builder()
            .id(1L)
            .recipeId(recipeId)
            .user(user)
            .comment("첫 번째 댓글")
            .build(),
        Comment.builder()
            .id(2L)
            .recipeId(recipeId)
            .user(user)
            .comment("두 번째 댓글")
            .build()
    );

    when(recipeSearchRepository.findById(recipeId)).thenReturn(Optional.of(RecipeDocument.builder().id(recipeId).build()));
    when(commentRepository.findAllByRecipeId(recipeId)).thenReturn(commentList);

    List<ReplyCommentGetDto> replyCommentList = Arrays.asList(
        ReplyCommentGetDto.builder().replyComment("첫 번째 대댓글").build(),
        ReplyCommentGetDto.builder().replyComment("두 번째 대댓글").build()
    );

    when(replyCommentService.getReplyCommentByComment(1L)).thenReturn(replyCommentList);
    when(replyCommentService.getReplyCommentByComment(2L)).thenReturn(Collections.emptyList());

    // when
    List<CommentGetDto> result = commentService.findAllCommentByRecipeId(recipeId);

    // then
    CommentGetDto firstComment = result.get(0);
    assertEquals(recipeId, firstComment.getRecipeId());
    assertEquals(1L, firstComment.getCommentId());
    assertEquals(user.getId(), firstComment.getUserId());
    assertEquals(user.getNickname(), firstComment.getUserNickname());
    assertEquals("첫 번째 댓글", firstComment.getComment());
    assertEquals(commentList.get(0).getCreatedAt(), firstComment.getCreatedAt());
    assertEquals(commentList.get(0).getModifiedAt(), firstComment.getModifiedAt());

    CommentGetDto secondComment = result.get(1);
    assertEquals(recipeId, secondComment.getRecipeId());
    assertEquals(2L, secondComment.getCommentId());
    assertEquals(user.getId(), secondComment.getUserId());
    assertEquals(user.getNickname(), secondComment.getUserNickname());
    assertEquals("두 번째 댓글", secondComment.getComment());
    assertEquals(commentList.get(1).getCreatedAt(), secondComment.getCreatedAt());
    assertEquals(commentList.get(1).getModifiedAt(), secondComment.getModifiedAt());
  }


  private Principal mockPrincipal(String userEmail) {
    return () -> userEmail;
  }
}