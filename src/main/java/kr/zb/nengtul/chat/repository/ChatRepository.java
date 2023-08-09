package kr.zb.nengtul.chat.repository;


import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(String roomId);

    List<Chat> findByChatRoomRoomIdOrderByCreatedAtAsc(String roomId);

    Optional<Chat> findByIdAndReadMarkIsFalse(Long chatId);

    void deleteAllByChatRoom(ChatRoom chatRoom);
}

