package kr.zb.nengtul.chat.repository;


import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @EntityGraph(value = "chatRoomWithShareBoardAndConnectedChatRooms")
    Optional<ChatRoom> findByRoomId(String roomId);
    @EntityGraph(value = "chatRoomWithShareBoardAndConnectedChatRoomsAndChtList")
    List<ChatRoom> findByConnectedChatRoomsUserIdAndConnectedChatRoomsLeaveRoomIsFalseOrderByChatListCreatedAtDesc(User user);
    List<ChatRoom> findByShareBoard(ShareBoard shareBoard);
}