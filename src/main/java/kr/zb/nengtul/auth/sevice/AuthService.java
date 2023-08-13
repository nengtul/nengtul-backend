package kr.zb.nengtul.auth.sevice;

import kr.zb.nengtul.auth.repository.BlacklistTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final BlacklistTokenRepository blacklistTokenRepository;


  //매월 1일 0시 0분 0초에 블랙리스트에 등록된 모든 토큰 삭제
  @Scheduled(cron = "0 0 0 1 * ?")
  public void cleanBlacklist() {
    blacklistTokenRepository.deleteAll();
    log.debug("** Clear " + blacklistTokenRepository.count() + "BlacklistToken **");
  }

}
