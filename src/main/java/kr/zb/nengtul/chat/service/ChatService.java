package kr.zb.nengtul.chat.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.dto.ChatDto;
import kr.zb.nengtul.chat.dto.ChatMessage;
import kr.zb.nengtul.chat.dto.ChatRoomListDto;
import kr.zb.nengtul.chat.repository.ChatRepository;
import kr.zb.nengtul.chat.repository.ChatRoomRepository;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public String getRoomId(Long sender, Long receiver) {

        if (Objects.equals(receiver, sender)) {
            throw new CustomException(ErrorCode.SELF_MESSAGE_FORBIDDEN);
        }

        // 채팅방이 이미 존재하는지 확인합니다.
        ChatRoom existingChatRoom = chatRoomRepository.findByUserId(sender, receiver);

        if (existingChatRoom == null) {
            // 채팅방이 존재하지 않으면 새로운 채팅방을 생성합니다.
            ChatRoom newChatRoom = new ChatRoom();
            newChatRoom.setUserId(new HashSet<>(Arrays.asList(receiver, sender)));
            newChatRoom = chatRoomRepository.save(newChatRoom);
            existingChatRoom = newChatRoom;
        }
        return existingChatRoom.getRoomId();
    }

    public List<ChatDto> joinChat(String roomId) {

        ChatRoom chatRoom = chatRoomRepository.findByRoomIdIs(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        List<Chat> chatList = chatRoom.getChatList(); //채팅 기록을 불러옵니다.

        return chatList.stream().map(ChatDto::fromEntity) // dto로 변환
                .toList();
    }

    public List<ChatRoomListDto> getUserOwnedChatRoom(Long userId) { // 유저가 소유한 채팅방 리스트들을 조회하는 메서드
        List<ChatRoom> chatRoomList = chatRoomRepository.findByUserId(userId);

        return chatRoomList.stream()
                .map(chatRoom -> ChatRoomListDto.fromEntity(chatRoom, userId))
                .collect(Collectors.toList());
    }

    public void saveChatMessage(String roomId, ChatMessage message){

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        chatRepository.save(Chat.builder()
                .roomId(chatRoom)
                .sender(message.getSender())
                .content(message.getContent())
                .build());
    }


}
