package kr.zb.nengtul.user.service;

import java.security.Principal;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserLoginDto;
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
  public String join(UserJoinDto userJoinDto) throws Exception {
    // 이메일 중복 확인
    if (userRepository.existsByEmail(userJoinDto.getEmail())) {
      throw new Exception();
    } else if (userJoinDto.getPassword().length() < 8) {
      throw new Exception();
    }

    User user = User.builder()
        .name(userJoinDto.getName())
        .nickname(userJoinDto.getNickname())
        .password(passwordEncoder.encode(userJoinDto.getPassword()))
        .phoneNumber(userJoinDto.getPhoneNumber())
        .email(userJoinDto.getEmail())
        .address(userJoinDto.getAddress())
        .addressDetail(userJoinDto.getAddressDetail())
        .build();

    userRepository.save(user);

    return user.getName();
  }

//  public String login(UserLoginDto userLoginDto) throws Exception {
//    User user = userRepository.findByEmail(userLoginDto.getEmail())
//        .orElseThrow(Exception::new);
////    if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
////      System.out.println("비번틀림");
////      throw new RuntimeException("비밀번호 오류");
////    }
//
//    return jwtTokenProvider.createAccessToken(user.getEmail());
//  }

  public void quit(Principal principal) throws Exception {
    System.out.println(principal.getName());
    User user = userRepository.findByEmail(principal.getName()).orElseThrow(Exception::new);
    System.out.println(user.getId() + "///" + user.getEmail());
    userRepository.deleteById(user.getId());
  }


}
