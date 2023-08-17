package kr.zb.nengtul.user.domain.repository;

import java.util.Optional;
import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByRefreshToken(String refreshToken);

  Optional<User> findByProviderTypeAndSocialId(ProviderType providerType, String socialId);

  Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);

  Optional<User> findByEmailAndNameAndPhoneNumber(String email, String name, String phoneNumber);

  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByPhoneNumber(String phoneNumber);
}
