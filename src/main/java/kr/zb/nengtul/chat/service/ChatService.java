package kr.zb.nengtul.chat.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.repository.ChatRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat createMessage(User sender, ChatRoom chatRoom, String content) {
        Chat chat = Chat.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .content(content)
                .readMark(false)
                .build();
        return chatRepository.save(chat);
    }

    @Transactional
    public Chat markAsRead(Long chatId, User reader) {
        Chat chat = chatRepository.findByIdAndReadMarkIsFalse(chatId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT));
        if (!chat.getSender().getId().equals(reader.getId())) {
            chat.setReadMark(true);
            chatRepository.save(chat);
        }

        return chat;
    }

    @Transactional
    public List<Chat> markAllAsRead(String roomId, User reader) {
        List<Chat> chats = chatRepository.findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(
                roomId);
        List<Chat> markedChat = new ArrayList<>();
        for (Chat chat : chats) {
            if (!chat.getSender().getId().equals(reader.getId())) {
                chat.setReadMark(true);
                markedChat.add(chat);
            }
        }
        chatRepository.saveAll(markedChat);

        return markedChat;
    }

    public List<Chat> getPreviousMessages(String roomId) {
        return chatRepository.findByChatRoomRoomIdOrderByCreatedAtAsc(roomId);
    }

}
