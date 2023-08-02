package kr.zb.nengtul.chat.dto;

import kr.zb.nengtul.chat.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StartChat {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{

        private String roomId;
        private Long sender;
        private String content;


        public Response(Chat chat) {
            this.roomId = chat.getChatRoom().getRoomId();
            this.sender = chat.getSender().getId();
            this.content = chat.getContent();
        }
    }

}
