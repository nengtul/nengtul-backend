package kr.zb.nengtul.notice.domain.repository;

import kr.zb.nengtul.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
