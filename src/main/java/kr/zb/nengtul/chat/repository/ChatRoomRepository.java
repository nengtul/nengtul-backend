package kr.zb.nengtul.chat.repository;


import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    @Query(value = "SELECT * FROM chat_room c WHERE JSON_CONTAINS(c.user_id, CAST(:userId AS CHAR), '$')", nativeQuery = true)
    List<ChatRoom> findByUserId(Long userId);

    @Query(value = """
            SELECT * FROM chat_room c
            WHERE c.user_id = JSON_ARRAY(:sender, :receiver)
            OR c.user_id = JSON_ARRAY(:receiver, :sender)
            """, nativeQuery = true)
    ChatRoom findByUserId(Long sender, Long receiver);


    Optional<ChatRoom> findByRoomId(String roomId);
    @EntityGraph(value = "chatRoomWithChatList", type = EntityGraph.EntityGraphType.LOAD)
    Optional<ChatRoom> findByRoomIdIs(String roomId);

}