package kr.zb.nengtul.chat.controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.chat.dto.ChatDto;
import kr.zb.nengtul.chat.dto.ChatRoomDto;
import kr.zb.nengtul.chat.service.ChatRoomService;
import kr.zb.nengtul.chat.service.ChatService;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ShareBoardService shareBoardService;
    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/start/shareboards/{shareBoardId}")
    public void startChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("shareBoardId") Long shareBoardId,
            String content
    ) {

        String email = userService.getEmailByAccessor(accessor);
        User sender = userService.findUserByEmail(email);

        ShareBoard shareBoard = shareBoardService.findById(shareBoardId);
        User receiver = shareBoard.getUser();

        if (Objects.equals(receiver.getId(), sender.getId())) {
            throw new CustomException(ErrorCode.CANNOT_OPEN_CHATROOM_YOURSELF);
        }

        ChatRoom chatRoom = chatRoomService.findOrCreateRoom(sender, receiver, shareBoard);
        Chat message = chatService.createMessage(sender, chatRoom, content);

        simpMessagingTemplate.convertAndSend("/sub/chat/start/users/" + sender.getId(),
                ChatDto.fromEntity(message));

        simpMessagingTemplate.convertAndSend("/sub/chat/push/users/" + receiver.getId(),
                ChatDto.fromEntity(message));

        simpMessagingTemplate.convertAndSend("/sub/chat/send/rooms/" + chatRoom.getRoomId(),
                ChatDto.fromEntity(message));
    }


    @MessageMapping("/chat/send/rooms/{roomId}")
    public void sendMessage(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable String roomId,
            String content
    ) {

        String email = userService.getEmailByAccessor(accessor);
        User sender = userService.findUserByEmail(email);

        ChatRoom chatRoom = chatRoomService.findById(roomId);
        Chat message = chatService.createMessage(sender, chatRoom, content);

        List<User> connectedUsers = chatRoom.getConnectedChatRooms().stream()
                .map(ConnectedChatRoom::getUser)
                .toList();

        simpMessagingTemplate.convertAndSend("/sub/chat/send/rooms/" + roomId,
                ChatDto.fromEntity(message));

        connectedUsers.stream()
                .filter(user -> !user.getId().equals(sender.getId()))  // 자신에게는 보내지 않음
                .forEach(user -> simpMessagingTemplate.convertAndSend(
                        "/sub/chat/push/users/" + user.getId(),
                        ChatDto.fromEntity(message)));
    }

    @MessageMapping("/chat/get/rooms/{roomId}")
    public void getPreviousMessages(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable String roomId
    ) {

        String email = userService.getEmailByAccessor(accessor);
        User user = userService.findUserByEmail(email);

        List<Chat> messages = chatService.getPreviousMessages(roomId);

        simpMessagingTemplate.convertAndSend("/sub/chat/get/users/" + user.getId(),
                messages.stream().map(ChatDto::fromEntity).toList());
    }

    @MessageMapping("/chat/mark/rooms/{roomId}/chats/{chatId}")
    public void markAsRead(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable Long chatId,
            @DestinationVariable String roomId
    ) {
        String email = userService.getEmailByAccessor(accessor);
        User reader = userService.findUserByEmail(email);

        Chat message = chatService.markAsRead(chatId, reader);

        simpMessagingTemplate.convertAndSend("/sub/chat/mark/rooms/" + roomId,
                ChatDto.fromEntity(message));
    }

    @MessageMapping("/chat/mark-all/rooms/{roomId}")
    public void markAllAsRead(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable String roomId
    ) {
        String email = userService.getEmailByAccessor(accessor);
        User reader = userService.findUserByEmail(email);

        List<Chat> markedMessages = chatService.markAllAsRead(roomId, reader);

        simpMessagingTemplate.convertAndSend("/sub/chat/mark-all/rooms/" + roomId,
                markedMessages.stream().map(ChatDto::fromEntity).toList());
    }

    @DeleteMapping("/v1/chat/leave/rooms/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(
            Principal principal,
            @PathVariable String roomId
    ) {
        User user = userService.findUserByEmail(principal.getName());
        chatRoomService.leaveChatRoom(user, roomId);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/v1/chat/list")
    public ResponseEntity<List<ChatRoomDto>> getChatRoomList(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        List<ChatRoom> chatRoomList = chatRoomService.getChatRoomList(user);

        return ResponseEntity.ok(
                chatRoomList.stream()
                        .map(chatRoom -> ChatRoomDto.fromEntity(chatRoom, user))
                        .toList());

    }
}

