package kr.zb.nengtul.chat.repository;

import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConnectedChatRoomRepository extends JpaRepository<ConnectedChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.shareBoard.id = :shareBoardId AND EXISTS (SELECT ucr1 FROM ConnectedChatRoom ucr1 WHERE ucr1.chatRoom = cr AND ucr1.userId = :user1) AND EXISTS (SELECT ucr2 FROM ConnectedChatRoom ucr2 WHERE ucr2.chatRoom = cr AND ucr2.userId = :user2)")
    Optional<ChatRoom> findChatRoomByUsersAndShareBoard(User user1, User user2, Long shareBoardId);

    List<ConnectedChatRoom> findByChatRoomRoomId(String roomId);
}
