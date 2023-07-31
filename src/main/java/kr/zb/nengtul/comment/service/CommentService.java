package kr.zb.nengtul.comment.service;

import java.security.Principal;
import kr.zb.nengtul.comment.domain.dto.CommentReqDto;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
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
  private final CommentRepository commentRepository;
  private final UserService userService;

  @Transactional
  public void createComment(Long recipeId, CommentReqDto commentReqDto, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Comment comment = Comment.builder()
        .recipeId(recipeId)
        .user(user)
        .content(commentReqDto.getContent())
        .build();
    commentRepository.save(comment);
  }
}
