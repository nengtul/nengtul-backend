package kr.zb.nengtul.user.service;

import java.security.Principal;
import kr.zb.nengtul.global.jwt.JwtToken;
import kr.zb.nengtul.global.jwt.JwtTokenProvider;
import kr.zb.nengtul.user.entity.domain.User;
import kr.zb.nengtul.user.entity.dto.UserJoinDto;
import kr.zb.nengtul.user.entity.dto.UserLoginDto;
import kr.zb.nengtul.user.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  public String login(UserLoginDto userLoginDto) throws NotFoundException {
    User user = userRepository.findByEmail(userLoginDto.getEmail())
        .orElseThrow(NotFoundException::new);
    if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
      System.out.println("비번틀림");
      throw new NotFoundException();
    }

    return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
  }

  public void quit(Principal principal) throws NotFoundException {
    System.out.println(principal.getName());
    User user = userRepository.findByEmail(principal.getName()).orElseThrow(NotFoundException::new);
    System.out.println(user.getId() + "///" + user.getEmail());
    userRepository.deleteById(user.getId());
  }


  @Transactional
  public String join(UserJoinDto userJoinDto) throws NotFoundException {
//    String encodedPassword = passwordEncoder.encode(userJoinDto.getPassword());

//    // 아이디 중복 확인
//    if (userRepository.existsCustomerById(customerDto.getId())) {
//      throw new DuplicateIdException();
//    }
//
    // 이메일 중복 확인
    if (userRepository.existsByEmail(userJoinDto.getEmail())) {
      throw new NotFoundException();
    } else if (userJoinDto.getPassword().length() < 8) {
      throw new NotFoundException();
    }

    User user = User.builder()
//        .loginId(userJoinDto.getLoginId())
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

}
