package kr.zb.nengtul.shareboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.global.entity.RoleType;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.shareboard.domain.dto.ShareBoardDto;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.domain.repository.ShareBoardRepository;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import kr.zb.nengtul.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

@DisplayName("나눔게시판 테스트")
class ShareBoardServiceTest {

  private ShareBoardService shareBoardService;
  private UserService userService;
  private ShareBoardRepository shareBoardRepository;
  private AmazonS3Service amazonS3Service;

  @BeforeEach
  void setUp() {
    // Mock 객체 초기화
    shareBoardRepository = mock(ShareBoardRepository.class);
    amazonS3Service = mock(AmazonS3Service.class);
    UserRepository userRepository = mock(UserRepository.class);
    userService = mock(UserService.class);

    shareBoardService = new ShareBoardService(
        userRepository, userService, shareBoardRepository, amazonS3Service);
  }

  @Test
  @DisplayName("나눔 게시판 등록 성공")
  void createShareBoard_SUCCESS() {
    //given
    User newUser = new User();
    newUser.setEmailVerifiedYn(true);

    // userService.findUserByEmail 메서드에 대한 mock 설정
    when(userService.findUserByEmail(any())).thenReturn(newUser);

    when(shareBoardRepository.findById(any()))
        .thenReturn(Optional.of(new ShareBoard()));

    MultipartFile image = mock(MultipartFile.class);
    Principal principal = new UsernamePasswordAuthenticationToken(
        "aa@aa.aa", null);

    ShareBoardDto shareBoardDto = ShareBoardDto.builder()
        .title("제목1")
        .content("내용1")
        .place("집앞")
        .price(0L)
        .lon(1.1)
        .lat(1.1)
        .build();
    //when
    shareBoardService.createShareBoard(shareBoardDto, principal, image);
    //then
    verify(shareBoardRepository, times(1))
        .save(any(ShareBoard.class));
  }

  @Test
  @DisplayName("나눔 게시판 등록 실패 - 이메일 인증 X")
  void createShareBoard_FAIL_Verify_N() {
    //given
    User newUser = new User();
    newUser.setEmailVerifiedYn(false);

    // userService.findUserByEmail 메서드에 대한 mock 설정
    when(userService.findUserByEmail(any())).thenReturn(newUser);

    when(shareBoardRepository.findById(any()))
        .thenReturn(Optional.of(new ShareBoard()));

    MultipartFile image = mock(MultipartFile.class);
    Principal principal = new UsernamePasswordAuthenticationToken(
        "aa@aa.aa", null);

    ShareBoardDto shareBoardDto = ShareBoardDto.builder()
        .title("제목1")
        .content("내용1")
        .place("집앞")
        .price(0L)
        .lon(1.1)
        .lat(1.1)
        .build();

    //when&then

    //이메일 인증 안해서 CustomException 발생
    assertThrows(CustomException.class,
        () -> shareBoardService.createShareBoard(shareBoardDto, principal, image));
  }

  @Test
  @DisplayName("나눔 게시판 업데이트 성공")
  void updateShareBoard_SUCCESS() {
    //given
    User newUser = new User();
    newUser.setEmailVerifiedYn(true);

    // userService.findUserByEmail 메서드에 대한 mock 설정
    when(userService.findUserByEmail(any())).thenReturn(newUser);

    // shareBoardRepository.findById 메서드에 대한 mock 설정
    ShareBoard existingShareBoard = new ShareBoard();
    existingShareBoard.setUser(newUser); // 업데이트 시 user 비교를 위해 설정
    existingShareBoard.setShareImg("이미지1.jpg"); // 사진 한 장만 올리기
    when(shareBoardRepository.findById(any()))
        .thenReturn(Optional.of(existingShareBoard));

    // 이미지 리턴값 ""
    when(amazonS3Service.uploadFileForShareBoard(eq(null), anyLong())).thenReturn("");

    Principal principal = new UsernamePasswordAuthenticationToken("aa@aa.aa", null);

    ShareBoardDto shareBoardDto = ShareBoardDto.builder()
        .title("업데이트된 제목")
        .content("업데이트된 내용")
        .place("업데이트된 장소")
        .price(10000L)
        .lon(2.2)
        .lat(2.2)
        .build();

    //when
    shareBoardService.updateShareBoard(1L, shareBoardDto, principal, null);

    //then
    verify(shareBoardRepository, times(1)).findById(anyLong());
    verify(shareBoardRepository, times(1)).save(any(ShareBoard.class));
    assertEquals("업데이트된 제목", existingShareBoard.getTitle());
    assertEquals("업데이트된 내용", existingShareBoard.getContent());
    assertEquals("업데이트된 장소", existingShareBoard.getPlace());
    assertEquals(10000L, existingShareBoard.getPrice());
    assertEquals(2.2, existingShareBoard.getLat(), 0.0001);
    assertEquals(2.2, existingShareBoard.getLon(), 0.0001);
    assertEquals("이미지1.jpg", existingShareBoard.getShareImg());
  }

  @Test
  @DisplayName("나눔 게시판 업데이트 실패 - 작성자가 다른 경우")
  void updateShareBoard_FAIL_NotSameUser() {
    //given

    User existingUser = new User();
    existingUser.setEmail("aa@aa.aa");
    existingUser.setEmailVerifiedYn(true);

    User newUser = new User();
    newUser.setEmail("bb@aa.aa");
    newUser.setEmailVerifiedYn(true);

    // userService.findUserByEmail 메서드에 대한 mock 설정
    when(userService.findUserByEmail("aa@aa.aa")).thenReturn(existingUser);
    when(userService.findUserByEmail("bb@aa.aa")).thenReturn(newUser);

    // shareBoardRepository.findById 메서드에 대한 mock 설정
    ShareBoard existingShareBoard = new ShareBoard();
    existingShareBoard.setUser(existingUser); // 업데이트 시 user 비교를 위해 설정
    existingShareBoard.setShareImg("이미지1.jpg\\이미지2.jpg\\이미지3.jpg"); // 이미지 경로 설정
    when(shareBoardRepository.findById(any()))
        .thenReturn(Optional.of(existingShareBoard));

    MultipartFile image = mock(MultipartFile.class);
    Principal principal = new UsernamePasswordAuthenticationToken("bb@aa.aa", null);

    ShareBoardDto shareBoardDto = ShareBoardDto.builder()
        .title("업데이트된 제목")
        .content("업데이트된 내용")
        .place("업데이트된 장소")
        .price(10000L)
        .lon(2.2)
        .lat(2.2)
        .build();

    //when&then
    assertThrows(CustomException.class,
        () -> shareBoardService.updateShareBoard(1L, shareBoardDto, principal, image));
  }

  @Test
  @DisplayName("나눔 게시판 삭제 성공")
  void deleteShareBoard_SUCCESS() {
    //given
    Long shareBoardId = 1L;

    User user = new User();
    user.setEmail("aa@aa.aa");
    ShareBoard shareBoard = new ShareBoard();
    shareBoard.setId(shareBoardId);
    shareBoard.setUser(user);

    when(userService.findUserByEmail(any())).thenReturn(user);
    when(shareBoardRepository.findById(shareBoardId)).thenReturn(Optional.of(shareBoard));

    //when
    shareBoardService.deleteShareBoard(shareBoardId,
        new UsernamePasswordAuthenticationToken("aa@aa.aa", null));
    //then
    verify(shareBoardRepository, times(1)).delete(shareBoard);
  }

  @Test
  @DisplayName("나눔 게시판 삭제 실패 - 작성자와 다른 유저")
  void deleteShareBoard_FAIL_NotSameUser() {
    //given

    Long shareBoardId = 1L;

    String authorEmail = "aa@aa.aa";
    String otherUserEmail = "bb@bb.b";

    User authorUser = new User();
    authorUser.setEmail(authorEmail);
    authorUser.setRoles(RoleType.USER);

    User otherUser = new User();
    otherUser.setEmail(otherUserEmail);
    otherUser.setRoles(RoleType.USER);

    ShareBoard shareBoard = new ShareBoard();
    shareBoard.setId(shareBoardId);
    shareBoard.setUser(authorUser);

    when(userService.findUserByEmail(authorEmail)).thenReturn(authorUser);
    when(userService.findUserByEmail(otherUserEmail)).thenReturn(otherUser);

    when(shareBoardRepository.findById(shareBoardId)).thenReturn(Optional.of(shareBoard));

    Principal otherUserPrincipal = () -> otherUserEmail;

    // when & then
    assertThrows(CustomException.class, () -> shareBoardService.deleteShareBoard(shareBoardId, otherUserPrincipal));
  }

  @Test
  @DisplayName("나눔 게시판 목록 조회 성공 - closed가 null인 경우")
  void getShareBoardList_SUCCESS_ClosedNull() {
    //given
    double lat = 37.12345;
    double lon = 127.98765;
    double range = 0.1;

    List<ShareBoard> expectedShareBoardList = new ArrayList<>();
    ShareBoard shareBoard1 = new ShareBoard();
    shareBoard1.setId(1L);
    shareBoard1.setLat(37.11111);
    shareBoard1.setLon(127.88888);
    shareBoard1.setClosed(true); // closed 값을 true로 설정
    expectedShareBoardList.add(shareBoard1);

    ShareBoard shareBoard2 = new ShareBoard();
    shareBoard2.setId(2L);
    shareBoard2.setLat(37.22222);
    shareBoard2.setLon(127.99999);
    shareBoard2.setClosed(false); // closed 값을 false로 설정
    expectedShareBoardList.add(shareBoard2);

    // ShareBoardRepository.findByLatBetweenAndLonBetweenAndClosed 메서드에 대한 mock 설정
    when(shareBoardRepository.findByLatBetweenAndLonBetweenAndClosed(
        lat - range, lat + range, lon - range, lon + range, true))
        .thenReturn(Collections.singletonList(shareBoard1));

    when(shareBoardRepository.findByLatBetweenAndLonBetweenAndClosed(
        lat - range, lat + range, lon - range, lon + range, false))
        .thenReturn(Collections.singletonList(shareBoard2));

    // ShareBoardRepository.findByLatBetweenAndLonBetween 메서드에 대한 mock 설정
    when(shareBoardRepository.findByLatBetweenAndLonBetween(
        lat - range, lat + range, lon - range, lon + range))
        .thenReturn(expectedShareBoardList);

    //when
    List<ShareBoard> actualShareBoardList = shareBoardService.getShareBoardList(lat, lon, range,
        null);
    //then
    assertEquals(expectedShareBoardList, actualShareBoardList);
  }


  @Test
  @DisplayName("나눔 게시판 목록 조회 성공 - closed가 true인 경우")
  void getShareBoardList_SUCCESS_ClosedTrue() {
    //given
    double lat = 37.12345;
    double lon = 127.98765;
    double range = 0.1;
    boolean closed = true; // closed 값을 true로 설정

    List<ShareBoard> expectedShareBoardList = new ArrayList<>();
    ShareBoard shareBoard1 = new ShareBoard();
    shareBoard1.setId(1L);
    shareBoard1.setLat(37.11111);
    shareBoard1.setLon(127.88888);
    shareBoard1.setClosed(true); // closed 값을 true로 설정
    expectedShareBoardList.add(shareBoard1);

    // ShareBoardRepository.findByLatBetweenAndLonBetweenAndClosed 메서드에 대한 mock 설정
    when(shareBoardRepository.findByLatBetweenAndLonBetweenAndClosed(
        lat - range, lat + range, lon - range, lon + range, closed))
        .thenReturn(expectedShareBoardList);

    //when
    List<ShareBoard> actualShareBoardList = shareBoardService.getShareBoardList(lat, lon, range,
        closed);

    //then
    assertEquals(expectedShareBoardList, actualShareBoardList);
  }

  @Test
  @DisplayName("나눔 게시판 목록 조회 성공 - closed가 true인 경우")
  void getShareBoardList_SUCCESS_ClosedFalse() {
    //given
    double lat = 37.12345;
    double lon = 127.98765;
    double range = 0.1;
    boolean closed = false; // closed 값을 true로 설정

    List<ShareBoard> expectedShareBoardList = new ArrayList<>();
    ShareBoard shareBoard1 = new ShareBoard();
    shareBoard1.setId(1L);
    shareBoard1.setLat(37.11111);
    shareBoard1.setLon(127.88888);
    shareBoard1.setClosed(false); // closed 값을 true로 설정
    expectedShareBoardList.add(shareBoard1);

    // ShareBoardRepository.findByLatBetweenAndLonBetweenAndClosed 메서드에 대한 mock 설정
    when(shareBoardRepository.findByLatBetweenAndLonBetweenAndClosed(
        lat - range, lat + range, lon - range, lon + range, closed))
        .thenReturn(expectedShareBoardList);

    //when
    List<ShareBoard> actualShareBoardList = shareBoardService.getShareBoardList(lat, lon, range,
        closed);

    // then
    assertEquals(expectedShareBoardList, actualShareBoardList);
  }

}