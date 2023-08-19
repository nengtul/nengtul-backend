package kr.zb.nengtul.user.service;

import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_EXIST_EMAIL;
import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_EXIST_NICKNAME;
import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_EXIST_PHONENUMBER;
import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_VERIFIED;
import static kr.zb.nengtul.global.exception.ErrorCode.EXPIRED_CODE;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_USER;
import static kr.zb.nengtul.global.exception.ErrorCode.NO_CONTENT;
import static kr.zb.nengtul.global.exception.ErrorCode.WRONG_VERIFY_CODE;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import kr.zb.nengtul.auth.entity.BlacklistToken;
import kr.zb.nengtul.auth.repository.BlacklistTokenRepository;
import kr.zb.nengtul.comment.domain.entity.Comment;
import kr.zb.nengtul.comment.domain.respository.CommentRepository;
import kr.zb.nengtul.comment.replycomment.domain.entity.ReplyComment;
import kr.zb.nengtul.comment.replycomment.domain.repository.ReplyCommentRepository;
import kr.zb.nengtul.favorite.domain.repository.FavoriteRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.global.util.HeaderUtil;
import kr.zb.nengtul.likes.domain.repository.LikesRepository;
import kr.zb.nengtul.notice.domain.entity.Notice;
import kr.zb.nengtul.notice.domain.repository.NoticeRepository;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.domain.repository.RecipeSearchRepository;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.domain.repository.ShareBoardRepository;
import kr.zb.nengtul.user.domain.dto.UserDetailDto;
import kr.zb.nengtul.user.domain.dto.UserFindEmailReqDto;
import kr.zb.nengtul.user.domain.dto.UserFindPasswordDto;
import kr.zb.nengtul.user.domain.dto.UserJoinDto;
import kr.zb.nengtul.user.domain.dto.UserPasswordChangeDto;
import kr.zb.nengtul.user.domain.dto.UserUpdateDto;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.domain.repository.UserRepository;
import kr.zb.nengtul.user.mailgun.client.MailgunClient;
import kr.zb.nengtul.user.mailgun.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import s3bucket.service.AmazonS3Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final LikesRepository likesRepository;

  private final RecipeSearchRepository recipeSearchRepository;

  private final NoticeRepository noticeRepository;

  private final ReplyCommentRepository replyCommentRepository;

  private final ShareBoardRepository shareBoardRepository;

  private final CommentRepository commentRepository;
  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final BlacklistTokenRepository blacklistTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailgunClient mailgunClient;
  private final AmazonS3Service amazonS3Service;

  @Value("${spring.quit.email}")
  private String quitId;

  //회원가입 및 이메일 인증 발송
  @Transactional
  public void joinUser(UserJoinDto userJoinDto) {
    // validation
    if (userRepository.existsByEmail(userJoinDto.getEmail())) {
      throw new CustomException(ALREADY_EXIST_EMAIL);
    } else if (userRepository.existsByNickname(userJoinDto.getNickname())) {
      throw new CustomException(ALREADY_EXIST_NICKNAME);
    } else if (userRepository.existsByPhoneNumber(userJoinDto.getPhoneNumber())) {
      throw new CustomException(ALREADY_EXIST_PHONENUMBER);
    }
    userRepository.save(User.builder()
        .name(userJoinDto.getName())
        .nickname(userJoinDto.getNickname())
        .password(passwordEncoder.encode(userJoinDto.getPassword()))
        .phoneNumber(userJoinDto.getPhoneNumber())
        .email(userJoinDto.getEmail())
        .address(userJoinDto.getAddress())
        .addressDetail(userJoinDto.getAddressDetail())
        .profileImageUrl(null)
        .build());
  }

  //이메일 인증
  @Transactional
  public void verifyEmail(String email, String code) {
    User user = findUserByEmail(email);
    if (user.isEmailVerifiedYn()) {
      throw new CustomException(ALREADY_VERIFIED);
    } else if (!user.getVerificationCode().equals(code)) {
      throw new CustomException(WRONG_VERIFY_CODE);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(EXPIRED_CODE);
    }
    user.setEmailVerifiedYn(true);
  }

  //회원 탈퇴
  @Transactional
  public void quitUser(Principal principal) {
    User user = findUserByEmail(principal.getName());
    User quitUser = findUserByEmail(quitId);

    for (Comment comment : user.getCommentList()) {
      comment.setUser(quitUser);
      commentRepository.save(comment);
    }
    for (ReplyComment replyComment : user.getReplyCommentList()) {
      replyComment.setUser(quitUser);
      replyCommentRepository.save(replyComment);
    }
    for (ShareBoard shareBoard : user.getShareBoardList()) {
      shareBoard.setUser(quitUser);
      shareBoard.setClosed(true);
      shareBoardRepository.save(shareBoard);
    }
    for (Notice notice : user.getNoticeList()) {
      notice.setUser(quitUser);
      noticeRepository.save(notice);
    }
    for (Notice notice : user.getNoticeList()) {
      notice.setUser(quitUser);
      noticeRepository.save(notice);
    }
    for (RecipeDocument recipeDocument : recipeSearchRepository.findAllByUserId(user.getId())) {
      recipeDocument.setUserId(quitUser.getId());
      recipeSearchRepository.save(recipeDocument);
    }

    userRepository.deleteById(user.getId());
  }

  //회원 정보 수정
  @Transactional
  public void updateUser(Principal principal, UserUpdateDto userUpdateDto, MultipartFile image) {
    User user = findUserByEmail(principal.getName());

    // 닉네임 중복 체크
    if (!user.getNickname().equals(userUpdateDto.getNickname()) && userRepository.existsByNickname(
        userUpdateDto.getNickname())) {
      throw new CustomException(ALREADY_EXIST_NICKNAME);
    }

    // 휴대폰 번호 중복 체크
    if (!user.getPhoneNumber().equals(userUpdateDto.getPhoneNumber())
        && userRepository.existsByPhoneNumber(userUpdateDto.getPhoneNumber())) {
      throw new CustomException(ALREADY_EXIST_PHONENUMBER);
    }

    if (image != null) {
      if (user.getProfileImageUrl() != null) {
        // 이미지가 있을 경우 이미지 업데이트
        amazonS3Service.updateFile(image, user.getProfileImageUrl());
      } else {
        // 이미지가 없을 경우 새 이미지 업로드
        user.setProfileImageUrl(amazonS3Service.uploadFileForProfile(image, user.getEmail()));
      }
    }

    // 사용자 정보 업데이트
    user.setNickname(userUpdateDto.getNickname());
    user.setPhoneNumber(userUpdateDto.getPhoneNumber());
    user.setAddress(userUpdateDto.getAddress());
    user.setAddressDetail(userUpdateDto.getAddressDetail());

    userRepository.save(user);
  }

  //임시 비밀번호 발급(비밀번호 찾기)
  @Transactional
  public void getNewPassword(UserFindPasswordDto userFindPasswordDto) {
    //이메일 전송 (이메일, 이름, 휴대폰 번호 부여받음)
    User user = userRepository.findByEmailAndNameAndPhoneNumber(userFindPasswordDto.getEmail(),
            userFindPasswordDto.getName(), userFindPasswordDto.getPhoneNumber())
        .orElseThrow(() -> new CustomException(NO_CONTENT));

    String code = RandomStringUtils.random(10, true, true);

    //임시비밀번호 발급 및 이메일 전송 + db에 임시 비밀번호로 저장
    SendMailForm sendMailForm = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to(user.getEmail())
        .subject("냉털 임시 비밀번호 발급 이메일")
        .text(getPasswordEmailBody(user.getName(), code))
        .build();

    mailgunClient.sendEmail(sendMailForm);
    user.setPassword(passwordEncoder.encode(code));
    userRepository.save(user);
  }

  //인증코드 및 시간 설정
  @Transactional
  public void changeCustomerValidateEmail(Long customerId, String verificationCode) {
    User user = userRepository.findById(customerId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    user.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
    user.setVerificationCode(verificationCode);
    userRepository.save(user);
  }

  //인증코드 및 시간 재설정
  public void resetVerify(Long userId) {
    //유저 정보페이지에서 가져오기때문에 바로 get
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    //이미 인증한 사용자가 인증 메일 다시 보내지못하게
    if (user.isEmailVerifiedYn()) {
      throw new CustomException(ALREADY_VERIFIED);
    }
    verifyEmailForm(user.getEmail(), user.getName());
  }

  //이메일 인증 폼
  public void verifyEmailForm(String email, String name) {
    String code = RandomStringUtils.random(10, true, true);
    User user = findUserByEmail(email);
    SendMailForm sendMailForm = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to(email)
        .subject("냉털 계정 인증용 이메일")
        .text(getVerificationEmailBody(email, name, code))
        .build();

    mailgunClient.sendEmail(sendMailForm);
    changeCustomerValidateEmail(user.getId(), code);
  }

  //인증용 이메일
  private String getVerificationEmailBody(String email, String name, String code) {
    //TODO : HTML email 폼 적용 예정
    return
        "안녕하세요 " + name + " 회원님 ! 링크를 통해 냉장고를 털어라 이메일 인증을 진행해주세요. \n\n"
            //.append("http://localhost:8080/v1/user/verify?email=") //로컬
            + "https://nengtul.shop/v1/users/verify?email=" //배포
            + email
            + "&code="
            + code;
  }

  //비밀번호 찾기용 Email
  private String getPasswordEmailBody(String name, String code) {
    //TODO : HTML email 폼 적용 예정
    return "안녕하세요 " + name
        + " 님! 이메일에 작성된 임시 비밀번호를 통해 로그인해주세요. \n\n"
        + "임시 비밀번호를 통해 로그인 후 비밀번호를 꼭 변경해주세요. \n\n"
        + "임시 비밀번호 : "
        + code;
  }

  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }

  @Transactional
  public void changePassword(Principal principal, UserPasswordChangeDto userPasswordChangeDto) {
    User user = findUserByEmail(principal.getName());

    user.setPassword(passwordEncoder.encode(userPasswordChangeDto.getPassword()));
    userRepository.save(user);
  }


  public String getEmailByAccessor(SimpMessageHeaderAccessor accessor) {
    if (accessor.getSessionAttributes().get("user") == null) {
      throw new CustomException(ErrorCode.NOT_EXIST_USER_ATTRIBUTE_IN_WEBSOCKET_SESSION);
    }
    return (String) Objects.requireNonNull(accessor.getSessionAttributes())
        .get("user");
  }

  @Transactional(readOnly = true)
  public UserDetailDto buildUserDetailDto(User user) {
    return UserDetailDto.builder()
        .id(user.getId())
        .name(user.getName())
        .nickname(user.getNickname())
        .phoneNumber(user.getPhoneNumber())
        .profileImageUrl(user.getProfileImageUrl())
        .providerTYpe(user.getProviderType())
        .address(user.getAddress())
        .addressDetail(user.getAddressDetail())
        .roles(user.getRoles())
        .emailVerifiedYn(user.isEmailVerifiedYn())
        .point(user.getPoint())
        .myRecipe(recipeSearchRepository.countByUserId(user.getId()))
        .likeRecipe(likesRepository.countByUserId(user.getId()))
        .shareList(
            shareBoardRepository.countByUserIdAndClosed(user.getId(), false)) //거래중인 게시물 개수만 확인
        .favoriteList(favoriteRepository.countByUserId(user.getId()))
        .build();
  }

  @Transactional
  public void logout(HttpServletRequest request, Principal principal) {
    //헤더에서 토큰 가져와서 Bearer부분 제거후 사용
    String token = HeaderUtil.getAccessToken(request);

    // AccessToken을 블랙리스트에 추가
    blacklistTokenRepository.save(BlacklistToken.builder()
        .email(principal.getName())
        .blacklistToken(token)
        .build());
  }

  //가입한 이메일 찾기(아이디 찾기)
  public User findEmail(UserFindEmailReqDto userFindEmailReqDto) {
    return userRepository.findByNameAndPhoneNumber(userFindEmailReqDto.getName(),
            userFindEmailReqDto.getPhoneNumber())
        .orElseThrow(() -> new CustomException(NO_CONTENT));
  }
}
