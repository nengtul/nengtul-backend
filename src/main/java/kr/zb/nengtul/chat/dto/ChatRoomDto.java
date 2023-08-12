package kr.zb.nengtul.chat.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

    private String roomId;
    private List<String> memberNicknames;
    private String shareBoardTitle;
    private String latestChat;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {

        List<String> members = chatRoom.getConnectedChatRooms().stream()
                .map(connectedChatRoom -> connectedChatRoom.getUser().getNickname()).toList();

        Chat latestChat = chatRoom.getChatList().stream()
                .max(Comparator.comparing(Chat::getCreatedAt))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT));

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .memberNicknames(members)
                .shareBoardTitle(chatRoom.getShareBoard().getTitle())
                .latestChat(Objects.requireNonNull(latestChat).getContent())
                .build();
    }

}
