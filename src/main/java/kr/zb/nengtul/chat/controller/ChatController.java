package kr.zb.nengtul.chat.controller;

import java.util.List;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.dto.SendMessage;
import kr.zb.nengtul.chat.dto.SendMessage.Response;
import kr.zb.nengtul.chat.dto.StartChat;
import kr.zb.nengtul.chat.service.ChatRoomService;
import kr.zb.nengtul.chat.service.ChatService;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ShareBoardService shareBoardService;
    private final ChatService chatService;


    @MessageMapping("/chat/startChat/{shareBoardId}")
    @SendToUser("/queue/startChat")
    public StartChat.Response startChat(
            @AuthenticationPrincipal User sender,
            @DestinationVariable Long shareBoardId,
            @Payload String content
    ) {
        ShareBoard shareBoard = shareBoardService.findById(shareBoardId);
        User receiver = shareBoard.getUser();
        ChatRoom chatRoom = chatRoomService.findOrCreateRoom(sender, receiver, shareBoard);
        Chat message = chatService.createMessage(sender, chatRoom, content);

        return new StartChat.Response(message);
    }

    @MessageMapping("/chat/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public SendMessage.Response sendMessage(
            @AuthenticationPrincipal User sender,
            @DestinationVariable String roomId,
            @Payload String content
    ) {
        ChatRoom chatRoom = chatRoomService.findById(roomId);
        Chat message = chatService.createMessage(sender, chatRoom, content);

        return SendMessage.Response.fromEntity(message);
    }

    @MessageMapping("/chat/previousMessage/{roomId}")
    @SendToUser("/queue/previousMessage")
    public List<SendMessage.Response> getPreviousMessages(
            @DestinationVariable String roomId
    ) {
        List<Chat> messages = chatService.getPreviousMessages(roomId);
        return messages.stream().map(Response::fromEntity).toList();
    }

    @MessageMapping("/chat/markAsRead/{chatId}")
    public void markAsRead(@AuthenticationPrincipal User reader, @DestinationVariable Long chatId) {
        chatService.markAsRead(chatId, reader);
    }

    @MessageMapping("/chat/markAllAsRead/{roomId}")
    public void markAllAsRead(@AuthenticationPrincipal User reader,
            @DestinationVariable String roomId) {
        chatService.markAllAsRead(roomId, reader);
    }

}

