package kr.zb.nengtul.chat.dto;

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
public class ChatRoomListDto {

    private String roomId;
    private Long otherUser;

    public static ChatRoomListDto fromEntity(ChatRoom chatRoom, Long userId) {

        Long otherUserId = chatRoom.getUserId().stream()
                .filter(id -> !id.equals(userId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return ChatRoomListDto.builder()
                .roomId(chatRoom.getRoomId())
                .otherUser(otherUserId)
                .build();
    }

}
