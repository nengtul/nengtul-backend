package kr.zb.nengtul.shareboard.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_SHARE_BOARD;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_USER;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_VERIFY_EMAIL;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.notice.domain.dto.NoticeReqDto;
import kr.zb.nengtul.notice.domain.entity.Notice;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.domain.repository.ShareBoardRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShareBoardService {

  private final UserRepository userRepository;
  private final ShareBoardRepository shareBoardRepository;

  @Transactional
  public void create(ShareBoardDto shareBoardDto, Principal principal) {
    User user = findUserByEmail(principal.getName());
    if (!user.isEmailVerifiedYn()) {
      throw new CustomException(NOT_VERIFY_EMAIL);
    }
    ShareBoard shareBoard = ShareBoard.builder()
        .user(user)
        .title(shareBoardDto.getTitle())
        .content(shareBoardDto.getContent())
        .shareImg(shareBoardDto.getShareImg())
        .price(shareBoardDto.getPrice())
        .lat(shareBoardDto.getLat())
        .lon(shareBoardDto.getLon())
        .views(0L)
        .build();
    user.setPointAddShardBoard(user.getPoint());
    userRepository.saveAndFlush(user);
    shareBoardRepository.save(shareBoard);
  }
  @Transactional
  public void update(Long id, ShareBoardDto shareBoardDto, Principal principal) {
    ShareBoard shareBoard = shareBoardRepository.findById(id).orElseThrow(()->new CustomException(NOT_FOUND_SHARE_BOARD));
    User user = findUserByEmail(principal.getName());
    if(shareBoard.getUser() != user){
      throw new CustomException(NO_PERMISSION);
    }
    shareBoard.setTitle(shareBoardDto.getTitle());
    shareBoard.setContent(shareBoardDto.getContent());
    shareBoard.setShareImg(shareBoardDto.getShareImg());
    shareBoard.setPrice(shareBoardDto.getPrice());
    shareBoard.setLat(shareBoardDto.getLat());
    shareBoard.setLat(shareBoardDto.getLon());
    shareBoardRepository.save(shareBoard);
  }

  @Transactional
  public void delete(Long id, Principal principal) {
    ShareBoard shareBoard = shareBoardRepository.findById(id).orElseThrow(()->new CustomException(NOT_FOUND_SHARE_BOARD));
    User user = findUserByEmail(principal.getName());
    if(shareBoard.getUser() != user){
      throw new CustomException(NO_PERMISSION);
    }
    shareBoardRepository.delete(shareBoard);
  }

  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }

}
