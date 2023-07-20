package kr.zb.nengtul.user.entity.repository;

import java.util.Optional;
import kr.zb.nengtul.global.entity.ProviderType;
import kr.zb.nengtul.user.entity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  Optional<User> findByName(String name);
  Optional<User> findByRefreshToken(String refreshToken);
  Optional<User> findByProviderTypeAndSocialId(ProviderType providerType, String socialId);
  boolean existsByEmail(String email);
}
