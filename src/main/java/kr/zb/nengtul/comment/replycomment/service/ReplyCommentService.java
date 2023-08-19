package kr.zb.nengtul.comment.replycomment.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_COMMENT;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_REPLY_COMMENT;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentGetDto;
import kr.zb.nengtul.comment.replycomment.domain.dto.ReplyCommentReqDto;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
import kr.zb.nengtul.comment.replycomment.domain.repository.ReplyCommentRepository;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyCommentService {

  private final CommentRepository commentRepository;

  private final ReplyCommentRepository replyCommentRepository;
  private final UserService userService;

  @Transactional
  public void createReplyComment(Long commentId, ReplyCommentReqDto replyCommentReqDto,
      Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT));
    replyCommentRepository.save(ReplyComment.builder()
        .replyComment(replyCommentReqDto.getReplyComment())
        .user(user)
        .comment(comment)
        .build());
  }

  @Transactional
  public void updateReplyComment(Long commentId, Long replyCommentId,
      ReplyCommentReqDto replyCommentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    ReplyComment replyComment = replyCommentRepository.findByIdAndCommentId(
        replyCommentId, commentId).orElseThrow(() -> new CustomException(NOT_FOUND_REPLY_COMMENT));
    if (!user.equals(replyComment.getUser())) {
      throw new CustomException(NO_PERMISSION);
    }
    replyComment.setReplyComment(replyCommentReqDto.getReplyComment());
    replyCommentRepository.save(replyComment);
  }

  @Transactional
  public void deleteReplyComment(Long commentId, Long replyCommentId, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    ReplyComment replyComment = replyCommentRepository.findByIdAndCommentId(
        replyCommentId, commentId).orElseThrow(() -> new CustomException(NOT_FOUND_REPLY_COMMENT));
    if (user.equals(replyComment.getUser()) || user.getRoles().equals(RoleType.ADMIN)) {
      replyCommentRepository.delete(replyComment);
    } else {
      throw new CustomException(NO_PERMISSION);
    }
  }

  public List<ReplyCommentGetDto> getReplyCommentByComment(Long commentId) {
    List<ReplyComment> replyCommentList = replyCommentRepository.findByCommentId(commentId);
    return replyCommentList.stream().map(ReplyCommentGetDto::buildCommentGetDto)
        .collect(Collectors.toList());
  }
}
