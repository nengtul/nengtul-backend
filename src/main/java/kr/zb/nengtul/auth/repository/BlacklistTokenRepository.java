package kr.zb.nengtul.auth.repository;

import kr.zb.nengtul.auth.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

  BlacklistToken findByBlacklistToken(String blacklistToken);
}
