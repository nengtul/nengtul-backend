package kr.zb.nengtul.chat.repository;


import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByChatRoomRoomIdAndReadIsFalseOrderByCreatedAtDesc(String roomId);

    List<Chat> findByChatRoomRoomIdOrderByCreatedAtDesc(String roomId);

    Optional<Chat> findByIdAndReadIsFalse(Long chatId);
}

