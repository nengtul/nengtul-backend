package kr.zb.nengtul.chat.dto;

import java.util.List;
import kr.zb.nengtul.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

    String roomId;
    List<String> memberNicknames;
    String shareBoardTitle;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {

        List<String> members = chatRoom.getConnectedChatRooms().stream()
                .map(connectedChatRoom -> connectedChatRoom.getUser().getNickname()).toList();

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .memberNicknames(members)
                .shareBoardTitle(chatRoom.getShareBoard().getTitle())
                .build();
    }

}
