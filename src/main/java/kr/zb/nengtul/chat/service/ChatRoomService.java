package kr.zb.nengtul.chat.service;

import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.chat.repository.ChatRoomRepository;
import kr.zb.nengtul.chat.repository.ConnectedChatRoomRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ConnectedChatRoomRepository connectedChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(ShareBoard shareBoard) {
        ChatRoom chatRoom = new ChatRoom(shareBoard);
        return chatRoomRepository.save(chatRoom);
    }

    public void joinRoom(User user, ChatRoom chatRoom) {
        ConnectedChatRoom userChatRoom = ConnectedChatRoom.builder()
                .userId(user)
                .chatRoom(chatRoom)
                .build();

        connectedChatRoomRepository.save(userChatRoom);
    }

    @Transactional
    public ChatRoom findOrCreateRoom(User user1, User user2, ShareBoard shareBoard ) {
        return connectedChatRoomRepository.findChatRoomByUsersAndShareBoard(user1, user2,shareBoard.getId())
                .orElseGet(() -> {
                    ChatRoom chatRoom = createChatRoom(shareBoard);
                    joinRoom(user1, chatRoom);
                    joinRoom(user2, chatRoom);
                    return chatRoom;
                });
    }

    public ChatRoom findById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
    }

}
