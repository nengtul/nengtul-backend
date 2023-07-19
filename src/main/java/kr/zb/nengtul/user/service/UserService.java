package kr.zb.nengtul.user.service;

import static kr.zb.nengtul.global.exception.ErrorCode.CHECK_ID_AND_PW;
import static kr.zb.nengtul.global.exception.ErrorCode.NOT_FOUND_USER;
import static kr.zb.nengtul.global.exception.ErrorCode.SHORT_PASSWORD;

import java.security.Principal;
import java.util.Objects;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserUpdateDto;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Transactional
  public String join(UserJoinDto userJoinDto) {
    // 이메일 중복 확인
    if (userRepository.existsByEmail(userJoinDto.getEmail())) {
      throw new CustomException(CHECK_ID_AND_PW);
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

    return user.getName();
  }

  @Transactional
  public void quit(Principal principal) {
    System.out.println(principal.getName());
    User user = userRepository.findByEmail(principal.getName())
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    userRepository.deleteById(user.getId());
  }

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
}
