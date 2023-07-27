package kr.zb.nengtul.chat.dto;


import kr.zb.nengtul.chat.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private Long sender;
    private String content;

    public static ChatDto fromEntity(Chat chat){
        return ChatDto.builder()
                .sender(chat.getSender())
                .content(chat.getContent())
                .build();
    }

}
