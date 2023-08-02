package kr.zb.nengtul.chat.dto;

import kr.zb.nengtul.chat.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SendMessage {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private Long chatId;
        private Long sender;
        private String content;
        private boolean isRead;
        public static Response fromEntity(Chat chat) {
            return Response.builder()
                    .chatId(chat.getId())
                    .sender(chat.getSender().getId())
                    .content(chat.getContent())
                    .isRead(chat.isRead())
                    .build();
        }
    }

}
