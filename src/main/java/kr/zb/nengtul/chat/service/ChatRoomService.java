package kr.zb.nengtul.chat.service;

import java.util.List;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.chat.repository.ChatRepository;
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
    private final ChatRepository chatRepository;

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
    public ChatRoom findOrCreateRoom(User user1, User user2, ShareBoard shareBoard) {

        return connectedChatRoomRepository.findChatRoomByUsersAndShareBoard(user1, user2,
                        shareBoard.getId())
                .orElseGet(() -> {
                    ChatRoom chatRoom = createChatRoom(shareBoard);
                    joinRoom(user1, chatRoom);
                    joinRoom(user2, chatRoom);
                    return chatRoom;
                });
    }

    @Transactional
    public void leaveChatRoom(User user, String roomId) {
        List<ConnectedChatRoom> chatUsers = connectedChatRoomRepository.findByChatRoomRoomId(
                roomId);

        if (chatUsers.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        ChatRoom chatRoom = chatUsers.get(0).getChatRoom();

        List<ConnectedChatRoom> deletedUser = chatUsers.stream()
                .filter(connectedChatRoom -> connectedChatRoom.getUser().getId()
                        .equals(user.getId())).toList();

        connectedChatRoomRepository.deleteAll(deletedUser);
        chatUsers.removeAll(deletedUser);

        if(chatUsers.isEmpty()){
            chatRepository.deleteAllByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        }

    }

    public ChatRoom findById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    public List<ChatRoom> getChatList(User user) {
        return chatRoomRepository.findByConnectedChatRoomsUserIdOrderByCreatedAtDesc(user);
    }
}
