package kr.zb.nengtul.shareboard.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_SHARE_BOARD;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_VERIFY_EMAIL;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import java.util.List;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.domain.repository.ShareBoardRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShareBoardService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ShareBoardRepository shareBoardRepository;

    @Transactional
    public void createShareBoard(ShareBoardDto shareBoardDto, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        if (!user.isEmailVerifiedYn()) {
            throw new CustomException(NOT_VERIFY_EMAIL);
        }
        ShareBoard shareBoard = ShareBoard.builder()
                .user(user)
                .title(shareBoardDto.getTitle())
                .shareImg(shareBoardDto.getShareImg())
                .price(shareBoardDto.getPrice())
                .lat(shareBoardDto.getLat())
                .lon(shareBoardDto.getLon())
                .closed(false)
                .build();
        user.setPointAddShardBoard(user.getPoint());
        userRepository.saveAndFlush(user);
        shareBoardRepository.save(shareBoard);
    }

    @Transactional
    public void updateShareBoard(Long id, ShareBoardDto shareBoardDto, Principal principal) {
        ShareBoard shareBoard = shareBoardRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SHARE_BOARD));
        User user = userService.findUserByEmail(principal.getName());
        if (shareBoard.getUser() != user) {
            throw new CustomException(NO_PERMISSION);
        }
        shareBoard.setTitle(shareBoardDto.getTitle());
        shareBoard.setShareImg(shareBoardDto.getShareImg());
        shareBoard.setPrice(shareBoardDto.getPrice());
        shareBoard.setLat(shareBoardDto.getLat());
        shareBoard.setLat(shareBoardDto.getLon());
        shareBoardRepository.save(shareBoard);
    }

    @Transactional
    public void deleteShareBoard(Long id, Principal principal) {
        ShareBoard shareBoard = shareBoardRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SHARE_BOARD));
        User user = userService.findUserByEmail(principal.getName());
        if (shareBoard.getUser() != user) {
            throw new CustomException(NO_PERMISSION);
        }
        shareBoardRepository.delete(shareBoard);
    }

    @Transactional
    public List<ShareBoard> getShareBoardList(double lat, double lon, double range,
            Boolean closed) {
        List<ShareBoard> shareBoardList;
        if (closed == null) {
            shareBoardList = shareBoardRepository.findByLatBetweenAndLonBetween(
                    lat - range, lat + range, lon - range, lon + range);

        } else {
            shareBoardList = shareBoardRepository.findByLatBetweenAndLonBetweenAndClosed(
                    lat - range, lat + range, lon - range, lon + range, closed);

        }
        return shareBoardList;
    }

    public ShareBoard findById(Long shareBoardId) {
        return shareBoardRepository.findById(shareBoardId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SHARE_BOARD));
    }

}
