package kr.zb.nengtul.notice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.notice.domain.dto.NoticeListDto;
import kr.zb.nengtul.notice.domain.dto.NoticeReqDto;
import kr.zb.nengtul.notice.domain.entity.Notice;
import kr.zb.nengtul.notice.domain.repository.NoticeRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

@DisplayName("공지사항 테스트")
class NoticeServiceTest {

  private NoticeService noticeService;
  private UserService userService;
  private NoticeRepository noticeRepository;
  private AmazonS3Service amazonS3Service;

  @BeforeEach
  void setUp() {
    // Mock 객체 초기화
    noticeRepository = mock(NoticeRepository.class);
    amazonS3Service = mock(AmazonS3Service.class);
    noticeService = mock(NoticeService.class);
    userService = mock(UserService.class);

    noticeService = new NoticeService(
        userService, noticeRepository, amazonS3Service);
  }

  @Test
  @DisplayName("공지사항 작성 성공")
  void createNotice_SUCCESS() {
    // given
    User newUser = new User();
    newUser.setRoles(RoleType.ADMIN);
    newUser.setEmail("bb@bb.bb");

    // userService.findUserByEmail 메서드에 대한 mock 설정
    when(userService.findUserByEmail(any())).thenReturn(newUser);

    // noticeRepository.findById 메서드에 대한 mock 설정
    when(noticeRepository.findById(any())).thenReturn(Optional.of(new Notice()));

    // noticeRepository.save 메서드에 대한 mock 설정
    when(noticeRepository.save(any(Notice.class))).then(returnsFirstArg());

    List<MultipartFile> images = Collections.singletonList(mock(MultipartFile.class));
    Principal principal = new UsernamePasswordAuthenticationToken("bb@bb.bb", null);

    NoticeReqDto noticeDto = NoticeReqDto.builder()
        .title("공지사항")
        .content("테스트")
        .build();

    // when
    noticeService.createNotice(noticeDto, principal, images);

    // then
    verify(noticeRepository, times(1)).save(any(Notice.class));
  }

  @Test
  @DisplayName("공지사항 수정 성공")
  void updateNotice_SUCCESS() {
    // given
    Long noticeId = 1L;
    NoticeReqDto noticeReqDto = NoticeReqDto.builder()
        .title("수정된 제목")
        .content("수정된 내용")
        .build();

    Principal principal = new UsernamePasswordAuthenticationToken("bb@bb.bb", null);

    User existingUser = new User();
    existingUser.setEmail("bb@bb.bb");

    Notice existingNotice = Notice.builder()
        .id(noticeId)
        .title("원래 제목")
        .content("원래 내용")
        .noticeImg("이미지1.jpg\\이미지2.jpg\\이미지3.jpg")
        .user(existingUser)
        .build();

    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));
    when(userService.findUserByEmail(anyString())).thenReturn(existingUser);

    // when
    List<MultipartFile> updatedImages = Arrays.asList(
        mock(MultipartFile.class),
        mock(MultipartFile.class),
        mock(MultipartFile.class)
    );

    // when
    noticeService.updateNotice(noticeId, noticeReqDto, principal, updatedImages);

    // then
    verify(noticeRepository, times(1)).findById(noticeId);
    verify(noticeRepository, times(1)).save(existingNotice);
    assertEquals(noticeReqDto.getTitle(), existingNotice.getTitle());
    assertEquals(noticeReqDto.getContent(), existingNotice.getContent());
    assertEquals(existingUser, existingNotice.getUser());

    String[] updatedImageUrls = existingNotice.getNoticeImg().split("\\\\");
    for (int i = 0; i < updatedImages.size(); i++) {
      verify(amazonS3Service, times(1)).updateFile(eq(updatedImages.get(i)),
          eq(updatedImageUrls[i]));
    }
  }

  @Test
  @DisplayName("공지사항 삭제 성공")
  void deleteNotice_SUCCESS() {
    // given
    Long noticeId = 1L;
    String authorEmail = "bb@bb.bb";

    User authorUser = new User();
    authorUser.setEmail(authorEmail);

    Notice notice = Notice.builder()
        .user(authorUser)
        .build();

    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
    when(userService.findUserByEmail(authorEmail)).thenReturn(authorUser);

    // when
    noticeService.deleteNotice(noticeId,
        new UsernamePasswordAuthenticationToken(authorEmail, null));

    // then
    verify(noticeRepository, times(1)).deleteById(noticeId);
  }

  @Test
  @DisplayName("공지사항 삭제 실패 - 작성자가 아닌 경우")
  void deleteNotice_FAIL_NotSameUser() {
    // given
    Long noticeId = 1L;
    String authorEmail = "aa@aa.aa";
    String currentUserEmail = "bb@bb.bb";

    User authorUser = new User();
    authorUser.setEmail(authorEmail);

    User currentUser = new User();
    currentUser.setEmail(currentUserEmail);

    Notice notice = Notice.builder()
        .user(authorUser)
        .build();

    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(notice));
    when(userService.findUserByEmail(currentUserEmail)).thenReturn(currentUser);

    // when, then
    assertThrows(CustomException.class, () -> noticeService.deleteNotice(noticeId,
        new UsernamePasswordAuthenticationToken(authorEmail, null)));
  }

  @Test
  @DisplayName("공지사항 목록 조회 성공")
  void getNoticeList_SUCCESS() {
    // given
    List<Notice> noticeList = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      User user = new User();
      user.setNickname("작성자 " + i);

      Notice notice = Notice.builder()
          .title("제목 " + i)
          .content("내용 " + i)
          .user(new User())
          .build();
      noticeList.add(notice);
    }
    when(noticeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(noticeList));

    // when
    Page<Notice> resultPage = noticeService.getNoticeList(Pageable.unpaged());
    List<NoticeListDto> result = resultPage.map(NoticeListDto::buildNoticeListDto).toList();

    // then
    assertEquals(noticeList.size(), result.size());
    for (int i = 0; i < noticeList.size(); i++) {
      Notice notice = noticeList.get(i);
      NoticeListDto dto = result.get(i);
      assertEquals(notice.getId(), dto.getNoticeId());
      assertEquals(notice.getTitle(), dto.getTitle());
      assertEquals(notice.getContent(), dto.getContent());
      assertEquals(notice.getUser().getNickname(), dto.getNickname());
    }
  }

  @Test
  @DisplayName("공지사항 상세 조회 성공")
  void getNoticeDetails_SUCCESS() {
    // given
    Long noticeId = 1L;
    Notice existingNotice = Notice.builder()
        .id(noticeId)
        .title("제목")
        .content("내용")
        .viewCount(10L)
        .user(new User())
        .build();
    when(noticeRepository.findById(noticeId)).thenReturn(Optional.of(existingNotice));

    // when
    Notice resultNotice = noticeService.getNoticeDetails(noticeId);

    // then
    assertEquals(existingNotice.getId(), resultNotice.getId());
    assertEquals(existingNotice.getTitle(), resultNotice.getTitle());
    assertEquals(existingNotice.getContent(), resultNotice.getContent());
    assertEquals(existingNotice.getViewCount(), resultNotice.getViewCount()); // 조회수 증가 확인
  }
}