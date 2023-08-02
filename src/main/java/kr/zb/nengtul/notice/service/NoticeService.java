package kr.zb.nengtul.notice.service;

import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_NOTICE;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_PERMISSION;

import java.security.Principal;
import java.util.List;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.notice.domain.dto.NoticeReqDto;
import kr.zb.nengtul.notice.domain.entity.Notice;
import kr.zb.nengtul.notice.domain.repository.NoticeRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

  private final UserService userService;
  private final NoticeRepository noticeRepository;
  private final AmazonS3Service amazonS3Service;

  @Transactional
  public void createNotice(NoticeReqDto noticeReqDto, Principal principal,
      List<MultipartFile> images) {
    User user = userService.findUserByEmail(principal.getName());

    Notice notice = Notice.builder()
        .title(noticeReqDto.getTitle())
        .content(noticeReqDto.getContent())
        .user(user)
        .viewCount(0L)
        .build();

    noticeRepository.save(notice);
    notice.setNoticeImg(amazonS3Service.uploadFileForNotice(images, notice.getId()));

  }

  @Transactional
  public void updateNotice(Long noticeId, NoticeReqDto noticeReqDto, Principal principal,
      List<MultipartFile> images) {
    User user = userService.findUserByEmail(principal.getName());
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
    if (!notice.getNoticeImg().isEmpty() &&
        !images.get(0).isEmpty()) {

      String[] imageUrlArr = notice.getNoticeImg().split("\\\\");

      for (int i = 0; i < imageUrlArr.length; i++) {
        amazonS3Service.updateFile(images.get(i), imageUrlArr[i]);
      }
    }
    if (notice.getUser().equals(user)) {
      notice.setTitle(noticeReqDto.getTitle());
      notice.setContent(noticeReqDto.getContent());

      noticeRepository.save(notice);
    } else {
      throw new CustomException(NO_PERMISSION);
    }
  }


  @Transactional
  public void deleteNotice(Long noticeId, Principal principal) {
    User user = userService.findUserByEmail(principal.getName());
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
    if (notice.getUser().equals(user)) {
      noticeRepository.deleteById(noticeId);
    } else {
      throw new CustomException(NO_PERMISSION);
    }
  }

  public Page<Notice> getNoticeList(Pageable pageable) {
    return noticeRepository.findAll(pageable);
  }

  @Transactional
  public Notice getNoticeDetails(Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_NOTICE));
    notice.setViewCount(notice.getViewCount() + 1);
    noticeRepository.saveAndFlush(notice);

    return notice;
  }
}
