package kr.zb.nengtul.chat.repository;


import kr.zb.nengtul.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
