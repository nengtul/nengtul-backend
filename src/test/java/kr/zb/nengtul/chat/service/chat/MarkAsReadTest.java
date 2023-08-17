package kr.zb.nengtul.chat.service.chat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.zb.nengtul.chat.domain.Chat;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.repository.ChatRepository;
import kr.zb.nengtul.chat.service.ChatService;
import kr.zb.nengtul.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MarkAsReadTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @Captor
    private ArgumentCaptor<Chat> chatCaptor;

    private User sender;
    private ChatRoom chatRoom;

    @BeforeEach
    public void setup() {
        // 가상의 sender와 chatRoom 객체 생성
        sender = new User(123L);
        ChatRoom chatRoom = new ChatRoom();
    }

    @Test
    @DisplayName("채팅 읽음 처리 성공")
    public void testMarkAsRead_WhenChatExistsAndReaderIsNotSender_ShouldMarkAsRead() {
        // Given
        Long chatId = 1L;
        User reader = new User();
        Chat chat = Chat.builder()
                .id(chatId)
                .chatRoom(chatRoom)
                .sender(sender)
                .content("test")
                .readMark(false)
                .build();

        // When
        when(chatRepository.findByIdAndReadMarkIsFalse(chatId)).thenReturn(Optional.of(chat));
        chatService.markAsRead(chatId, reader);

        // Then
        verify(chatRepository).findByIdAndReadMarkIsFalse(chatId);
        verify(chatRepository).save(chatCaptor.capture());

        Chat capturedChat = chatCaptor.getValue();
        assertTrue(capturedChat.isReadMark());
        assertTrue(chat.isReadMark());
    }


    @Test
    @DisplayName("자신의 메세지는 읽음 처리 되지 않는다")
    public void testMarkAsRead_WhenChatExistsAndReaderIsSender_ShouldNotMarkAsRead() {
        // Given
        Long chatId = 1L;
        Chat chat = Chat.builder()
                .id(chatId)
                .sender(sender)
                .readMark(false)
                .build();

        // When
        when(chatRepository.findByIdAndReadMarkIsFalse(chatId)).thenReturn(java.util.Optional.of(chat));
        chatService.markAsRead(chatId, sender);

        // Then
        verify(chatRepository).findByIdAndReadMarkIsFalse(chatId);
        verify(chatRepository, never()).save(any());
        assertFalse(chat.isReadMark());
    }
}
