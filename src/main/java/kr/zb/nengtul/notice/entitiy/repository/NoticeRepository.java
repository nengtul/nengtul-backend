package kr.zb.nengtul.notice.entitiy.repository;

import kr.zb.nengtul.notice.entitiy.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
