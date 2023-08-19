package kr.zb.nengtul.chat.dto;

import java.util.Comparator;
import java.util.Objects;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.entity.User;
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
    private String receiverPhoto;
    private String receiverNickname;
    private String shareBoardMainPhoto;
    private String shareBoardTitle;
    private Long shareBoardPrice;
    private String latestChat;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom, User sender) {

        User receiver = chatRoom.getConnectedChatRooms().stream()
                .map(ConnectedChatRoom::getUser)
                .filter(user -> !user.getId().equals(sender.getId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OTHER_USER));

        Chat latestChat = chatRoom.getChatList().stream()
                .max(Comparator.comparing(Chat::getCreatedAt))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHAT));

        ShareBoard shareBoard = chatRoom.getShareBoard();

        String shareImg = "";
        String title = "삭제된 게시물 입니다";
        Long price = 0L;

        if (shareBoard != null) {
            shareImg = shareBoard.getShareImg();
            title = shareBoard.getTitle();
            price = shareBoard.getPrice();
        }

        return ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .receiverPhoto(receiver.getProfileImageUrl())
                .receiverNickname(receiver.getNickname())
                .shareBoardMainPhoto(shareImg)
                .shareBoardTitle(title)
                .shareBoardPrice(price)
                .latestChat(Objects.requireNonNull(latestChat).getContent())
                .build();
    }

}
