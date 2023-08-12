package kr.zb.nengtul.chat.dto;

import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private  String roomId;
    private Long chatId;
    private Long shareBoardId;
    private String shareBoardTitle;
    private String senderNickname;
    private String content;
    private boolean isRead;

    public static ChatDto fromEntity(Chat chat) {

        ShareBoard shareBoard = chat.getChatRoom().getShareBoard();

        return ChatDto.builder()
                .roomId(chat.getChatRoom().getRoomId())
                .chatId(chat.getId())
                .shareBoardId(shareBoard.getId())
                .shareBoardTitle(shareBoard.getTitle())
                .senderNickname(chat.getSender().getNickname())
                .content(chat.getContent())
                .isRead(chat.isReadMark())
                .build();
    }

}

