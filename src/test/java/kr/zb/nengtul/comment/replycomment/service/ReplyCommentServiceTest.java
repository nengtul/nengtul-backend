package kr.zb.nengtul.comment.replycomment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentReqDto;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
import kr.zb.nengtul.comment.replycomment.domain.repository.ReplyCommentRepository;
import kr.zb.nengtul.comment.service.CommentService;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@DisplayName("대댓글 테스트")
class ReplyCommentServiceTest {

  private CommentRepository commentRepository;
  private UserService userService;
  private ReplyCommentService replyCommentService;
  private ReplyCommentRepository replyCommentRepository;

  @BeforeEach
  void setUp() {
    replyCommentRepository = mock(ReplyCommentRepository.class);
    commentRepository = mock(CommentRepository.class);
    userService = mock(UserService.class);

    replyCommentService = new ReplyCommentService(
        commentRepository, replyCommentRepository, userService);
  }


  @Test
  @DisplayName("대댓글 작성 성공")
  void createReplyComment_SUCCESS() {
    // given
    Long commentId = 1L;
    String userEmail = "aa@aa.aa";
    ReplyCommentReqDto replyCommentReqDto = new ReplyCommentReqDto();
    replyCommentReqDto.setReplyComment("대댓글");

    User user = User.builder()
        .email(userEmail)
        .build();
    Comment comment = Comment.builder()
        .id(commentId)
        .build();

    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    ReplyComment replyComment = ReplyComment.builder()
        .replyComment(replyCommentReqDto.getReplyComment())
        .user(user)
        .comment(comment)
        .build();

    when(replyCommentRepository.save(any(ReplyComment.class))).thenReturn(replyComment);

    // when
    replyCommentService.createReplyComment(commentId, replyCommentReqDto, mockPrincipal(userEmail));

    // then
    ArgumentCaptor<ReplyComment> replyCommentCaptor = ArgumentCaptor.forClass(ReplyComment.class);
    verify(replyCommentRepository).save(replyCommentCaptor.capture());

    ReplyComment capturedReplyComment = replyCommentCaptor.getValue();
    assertEquals(commentId, capturedReplyComment.getComment().getId());
    assertEquals(user, capturedReplyComment.getUser());
    assertEquals(replyCommentReqDto.getReplyComment(), capturedReplyComment.getReplyComment());
  }


  @Test
  @DisplayName("대댓글 수정 성공")
  void updateReplyComment_SUCCESS() {
    // given
    Long commentId = 1L;
    Long replyCommentId = 10L;
    String userEmail = "aa@aa.aa";
    ReplyCommentReqDto replyCommentReqDto = new ReplyCommentReqDto();
    replyCommentReqDto.setReplyComment("수정된 대댓글");

    User user = User.builder()
        .email(userEmail)
        .build();
    ReplyComment replyComment = ReplyComment.builder()
        .id(replyCommentId)
        .replyComment("기존 대댓글")
        .user(user)
        .comment(Comment.builder().id(commentId).build())
        .build();

    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(replyCommentRepository.findByIdAndCommentId(replyCommentId, commentId)).thenReturn(Optional.of(replyComment));

    // when
    replyCommentService.updateReplyComment(commentId, replyCommentId, replyCommentReqDto, mockPrincipal(userEmail));

    // then
    ArgumentCaptor<ReplyComment> replyCommentCaptor = ArgumentCaptor.forClass(ReplyComment.class);
    verify(replyCommentRepository).save(replyCommentCaptor.capture());

    ReplyComment updatedReplyComment = replyCommentCaptor.getValue();
    assertEquals(replyCommentId, updatedReplyComment.getId());
    assertEquals(user, updatedReplyComment.getUser());
    assertEquals(replyCommentReqDto.getReplyComment(), updatedReplyComment.getReplyComment());
  }

  @Test
  @DisplayName("대댓글 삭제 성공")
  void deleteReplyComment_SUCCESS() {
    // given
    Long commentId = 1L;
    Long replyCommentId = 10L;
    String userEmail = "aa@aa.aa";
    User user = User.builder()
        .email(userEmail)
        .build();
    ReplyComment replyComment = ReplyComment.builder()
        .id(replyCommentId)
        .user(user)
        .comment(Comment.builder().id(commentId).build())
        .build();

    when(userService.findUserByEmail(userEmail)).thenReturn(user);
    when(replyCommentRepository.findByIdAndCommentId(replyCommentId, commentId)).thenReturn(Optional.of(replyComment));

    // when
    replyCommentService.deleteReplyComment(commentId, replyCommentId, mockPrincipal(userEmail));

    // then
    verify(replyCommentRepository).delete(replyComment);
  }

  private Principal mockPrincipal(String userEmail) {
    return new Principal() {
      @Override
      public String getName() {
        return userEmail;
      }
    };
  }
}