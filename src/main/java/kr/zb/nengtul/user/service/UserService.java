package kr.zb.nengtul.user.service;

import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_EXIST_EMAIL;
import static kr.zb.nengtul.global.exception.ErrorCode.ALREADY_VERIFIED;
import static kr.zb.nengtul.global.exception.ErrorCode.EXPIRED_CODE;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_USER;
import static kr.zb.nengtul.global.exception.ErrorCode.SHORT_PASSWORD;
import static kr.zb.nengtul.global.exception.ErrorCode.WRONG_VERIFY_CODE;

import java.security.Principal;
import java.time.LocalDateTime;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.dto.UserDetailDto;
import kr.zb.nengtul.user.entity.dto.UserFindEmailDto;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserUpdateDto;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import kr.zb.nengtul.user.mailgun.client.MailgunClient;
import kr.zb.nengtul.user.mailgun.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final MailgunClient mailgunClient;

  //회원가입 및 이메일 인증 발송
  @Transactional
  public String join(UserJoinDto userJoinDto) {
    // 이메일 중복 확인
    if (userRepository.existsByEmail(userJoinDto.getEmail())) {
      throw new CustomException(ALREADY_EXIST_EMAIL);
    } else if (userJoinDto.getPassword() == null || userJoinDto.getPassword().length() < 8) {
      throw new CustomException(SHORT_PASSWORD);
    }

    User user = User.builder()
        .name(userJoinDto.getName())
        .nickname(userJoinDto.getNickname())
        .password(passwordEncoder.encode(userJoinDto.getPassword()))
        .phoneNumber(userJoinDto.getPhoneNumber())
        .email(userJoinDto.getEmail())
        .address(userJoinDto.getAddress())
        .addressDetail(userJoinDto.getAddressDetail())
        .profileImageUrl(null)
        .build();
    userRepository.save(user);

    String code = RandomStringUtils.random(10, true, true);

    SendMailForm sendMailForm = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to(userJoinDto.getEmail())
        .subject("냉털 계정 인증용 이메일")
        .text(getVerificationEmailBody(userJoinDto.getEmail(), userJoinDto.getName(), code))
        .build();

    mailgunClient.sendEmail(sendMailForm);
    changeCustomerValidateEmail(user.getId(), code);
    return "회원가입에 성공하였습니다. 이메일을 확인하고 이메일 인증을 진행해 주세요.";
  }

  //이메일 인증
  @Transactional
  public void verify(String email, String code) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    if (user.isEmailVerifiedYn()) {
      throw new CustomException(ALREADY_VERIFIED);
    } else if (!user.getVerificationCode().equals(code)) {
      throw new CustomException(WRONG_VERIFY_CODE);
    } else if (user.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(EXPIRED_CODE);
    }
    user.setEmailVerifiedYn(true);
  }

  //유저 상세보기
  public UserDetailDto getUserDetail(Principal principal) {
    User user = userRepository.findByEmail(principal.getName()).get();

    return UserDetailDto.buildUserDetailDto(user);
  }

  //회원 탈퇴
  @Transactional
  public void quit(Principal principal) {
    System.out.println(principal.getName());
    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    userRepository.deleteById(user.getId());
  }

  //회원 정보 수정
  @Transactional
  public String update(Principal principal, UserUpdateDto userUpdateDto) {
    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    if (userUpdateDto.getPassword() == null || userUpdateDto.getPassword().length() < 8) {
      throw new CustomException(SHORT_PASSWORD);
    }

    String updateProfileImageUrl =
        userUpdateDto.getProfileImageUrl() == null ? "" : userUpdateDto.getProfileImageUrl();

    user.setNickname(userUpdateDto.getNickname());
    user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
    user.setPhoneNumber(userUpdateDto.getPhoneNumber());
    user.setAddress(userUpdateDto.getAddress());
    user.setAddressDetail(userUpdateDto.getAddressDetail());
    user.setProfileImageUrl(updateProfileImageUrl);

    userRepository.save(user);
    return user.getEmail();
  }

  //인증용 이메일 (인증요청시 온 버튼을 어떻게 put으로 보내지??)
  private String getVerificationEmailBody(String email, String name, String code) {
    StringBuilder builder = new StringBuilder();
    return builder.append("안녕하세요 ").append(name).append("! 링크를 통해 이메일 인증을 진행해주세요. \n\n")
        .append("http://localhost:8080/v1/nengtul/user/verify?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }

  //비밀번호 찾기용 Email
  private String getPasswordEmailBody(String name, String code) {
    StringBuilder builder = new StringBuilder();
    return builder.append("안녕하세요 ").append(name)
        .append(" 님! 이메일에 작성된 임시 비밀번호를 통해 로그인해주세요. \n\n")
        .append("임시 비밀번호를 통해 로그인 후 비밀번호를 꼭 변경해주세요. \n\n")
        .append("임시 비밀번호 : ")
        .append(code).toString();
  }

  //인증코드 및 시간 설정
  @Transactional
  public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
    User user = userRepository.findById(customerId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

    user.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
    user.setVerificationCode(verificationCode);
    userRepository.save(user);
    return user.getVerifyExpiredAt();
  }

  //인증코드 및 시간 재설정
  public void resetVerify(Long userId) {
    //유저 정보페이지에서 가져오기때문에 바로 get
    User user = userRepository.findById(userId).get();
    String code = RandomStringUtils.random(10, true, true);

    SendMailForm sendMailForm = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to(user.getEmail())
        .subject("냉털 계정 인증용 이메일")
        .text(getVerificationEmailBody(user.getEmail(), user.getName(), code))
        .build();

    mailgunClient.sendEmail(sendMailForm);
    changeCustomerValidateEmail(user.getId(), code);
  }

  //가입한 이메일 찾기(아이디 찾기)
  public String findEmail(UserFindEmailDto userFindEmailDto) {
    User user = userRepository.findByName(userFindEmailDto.getName())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    if (!user.getPhoneNumber().equals(userFindEmailDto.getPhoneNumber())) {
      throw new CustomException(NOT_FOUND_USER);
    }
    return user.getEmail();
  }

  //임시 비밀번호 발급
  @Transactional
  public String findPasswordForSendMail(String email) {
    //이메일 전송
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    //이메일 verify 진행
    resetVerify(user.getId());
    //verify 하면서 임시 비밀번호 발급
    String code = RandomStringUtils.random(10, true, true);

    SendMailForm sendMailForm = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to(user.getEmail())
        .subject("냉털 계정 인증용 이메일")
        .text(getPasswordEmailBody(user.getName(), code))
        .build();

    mailgunClient.sendEmail(sendMailForm);
    changeCustomerValidateEmail(user.getId(), code);
    user.setPassword(passwordEncoder.encode(code));
    userRepository.save(user);
    return "이메일을 통해 임시 비밀번호가 발급되었습니다. 임시 비밀번호를 통해 로그인해주세요.";
  }
}
