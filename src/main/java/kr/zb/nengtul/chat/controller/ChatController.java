package kr.zb.nengtul.chat.controller;

import java.util.List;
import kr.zb.nengtul.chat.dto.ChatDto;
import kr.zb.nengtul.chat.dto.ChatMessage;
import kr.zb.nengtul.chat.dto.ChatRoomDto;
import kr.zb.nengtul.chat.dto.ChatRoomListDto;
import kr.zb.nengtul.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;


    @Transactional
    @MessageMapping("/chat/getRoomId") // roomId 생성 요청
    public void getRoomId(ChatRoomDto chatRoomDto) {
        Long receiver = chatRoomDto.getReceiver();
        Long sender = chatRoomDto.getSender();

        String roomId = chatService.getRoomId(sender, receiver);

        // 해당되거나 새로운 채팅방의 roomId를 보내줍니다.
        messagingTemplate.convertAndSend("/topic/roomId/" + sender.toString(),
                roomId);
    }

    @MessageMapping("/chat/join/{roomId}") // 채팅방 입장
    public void joinChat(@DestinationVariable String roomId) {

        List<ChatDto> chatList = chatService.joinChat(roomId); //이전 채팅기록을 전달해줍니다.

        messagingTemplate.convertAndSend("/topic/chatHistory/" + roomId, chatList);
    }

    @GetMapping("v1/chat/list/{userId}") // 유저가 소유한 채팅방 리스트
    public List<ChatRoomListDto> getUserOwnedChatRoom(@PathVariable Long userId) {
        return chatService.getUserOwnedChatRoom(userId);
    }


    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ChatMessage sendChatMessage(@DestinationVariable String roomId, ChatMessage message) {
        chatService.saveChatMessage(roomId, message);
        return message;

    }


}

