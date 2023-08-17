package kr.zb.nengtul.chat.service.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class MarkAllAsReadTest {

    @Captor
    ArgumentCaptor<List<Chat>> chatCaptor;
    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private ChatService chatService;
    private User sender;
    private ChatRoom chatRoom;
    private String roomId;
    private User reader;

    @BeforeEach
    public void setup() {
        // 가상의 sender, chatRoom, roomId, reader, pageable 객체 생성
        sender = new User(123L);
        roomId = "12345";
        reader = new User(456L);
    }


    @Test
    @DisplayName("상대방이 읽어야만 읽음처리 완료")
    public void testMarkAllAsRead_WhenChatsExistAndReaderIsNotSender_ShouldMarkAsRead() {
        // Given
        Chat chat1 = Chat.builder() //읽음처리 될 메세지
                .id(1L)
                .sender(sender)
                .chatRoom(chatRoom)
                .content("test")
                .readMark(false)
                .build();
        Chat chat2 = Chat.builder() // 읽음처리 되지 않음
                .id(2L)
                .sender(reader)
                .chatRoom(chatRoom)
                .content("test")
                .readMark(false)
                .build();
        List<Chat> chats = new ArrayList<>(Arrays.asList(chat1, chat2));

// When
        when(chatRepository.findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(
                roomId)).thenReturn(chats);
        chatService.markAllAsRead(roomId, reader);

// Then
        verify(chatRepository).findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(roomId);
        verify(chatRepository).saveAll(chatCaptor.capture());

        List<Chat> capturedChats = chatCaptor.getValue();
        assertEquals(1, capturedChats.size()); //1개만 업데이트 됨
        assertTrue(capturedChats.get(0).isReadMark());

        assertTrue(chats.get(0).isReadMark());
        assertFalse(chats.get(1).isReadMark()); // reader 자신의 메세지는 읽음처리 되지않음

    }

    @Test
    @DisplayName("자신이 보낸 메세지는 읽음 처리되지 않음")
    public void testMarkAllAsRead_WhenChatsExistAndReaderIsSender_ShouldNotMarkAsRead() {
        // Given
        Chat chat1 = Chat.builder()
                .id(1L)
                .sender(sender)
                .readMark(false)
                .build();
        Chat chat2 = Chat.builder()
                .id(2L)
                .sender(sender)
                .readMark(false)
                .build();
        List<Chat> chats = new ArrayList<>(Arrays.asList(chat1, chat2));

        // When
        when(chatRepository.findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(
                roomId)).thenReturn(chats);
        chatService.markAllAsRead(roomId, sender);

        // Then
        verify(chatRepository).findByChatRoomRoomIdAndReadMarkIsFalseOrderByCreatedAtAsc(roomId);
        verify(chatRepository, never()).save(any());
    }
}
