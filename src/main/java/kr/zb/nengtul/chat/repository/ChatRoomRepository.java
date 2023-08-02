package kr.zb.nengtul.chat.repository;


import java.util.Optional;
import kr.zb.nengtul.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    Optional<ChatRoom> findByRoomId(String roomId);
}