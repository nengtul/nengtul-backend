package kr.zb.nengtul.chat.service.chat_room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.chat.domain.ChatRoom;
import kr.zb.nengtul.chat.domain.ConnectedChatRoom;
import kr.zb.nengtul.chat.repository.ChatRoomRepository;
import kr.zb.nengtul.chat.repository.ConnectedChatRoomRepository;
import kr.zb.nengtul.chat.service.ChatRoomService;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @Captor
    ArgumentCaptor<ConnectedChatRoom> connectedChatRoomCaptor;
    @InjectMocks
    private ChatRoomService chatRoomService;
    @Mock
    private ConnectedChatRoomRepository connectedChatRoomRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;

    private final ShareBoard shareBoard = new ShareBoard();

    @DisplayName("ChatRoom 생성 테스트")
    @Test
    public void testCreateChatRoom() {
        // given
        ChatRoom chatRoom = new ChatRoom(shareBoard);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        // when
        ChatRoom result = chatRoomService.createChatRoom(shareBoard);

        // then
        assertNotNull(result);
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));

        assertEquals(chatRoom, result);
    }


    @DisplayName("ChatRoom 참가 테스트")
    @Test
    public void testJoinRoom() {
        // given
        User user = new User();
        ChatRoom chatRoom = new ChatRoom(shareBoard);

        // when
        chatRoomService.joinRoom(user, chatRoom);

        // then
        verify(connectedChatRoomRepository, times(1)).save(any(ConnectedChatRoom.class));

        verify(connectedChatRoomRepository, times(1)).save(connectedChatRoomCaptor.capture());
        assertEquals(user, connectedChatRoomCaptor.getValue().getUserId());
        assertEquals(chatRoom, connectedChatRoomCaptor.getValue().getChatRoom());
    }


    @DisplayName("ChatRoom 찾기 또는 생성 테스트")
    @Test
    public void testFindOrCreateRoom() {
        // given
        User user1 = new User();
        User user2 = new User();
        ChatRoom chatRoom = new ChatRoom(shareBoard);
        when(connectedChatRoomRepository.findChatRoomByUsersAndShareBoard(user1, user2,
                shareBoard.getId())).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);

        // when
        ChatRoom result = chatRoomService.findOrCreateRoom(user1, user2, shareBoard);

        // then
        assertNotNull(result);
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
        verify(connectedChatRoomRepository, times(2)).save(connectedChatRoomCaptor.capture());


        List<ConnectedChatRoom> capturedConnectedChatRooms = connectedChatRoomCaptor.getAllValues();
        assertEquals(2, capturedConnectedChatRooms.size());
        assertEquals(user1, capturedConnectedChatRooms.get(0).getUserId());
        assertEquals(user2, capturedConnectedChatRooms.get(1).getUserId());
        assertEquals(chatRoom, capturedConnectedChatRooms.get(0).getChatRoom());
        assertEquals(chatRoom, capturedConnectedChatRooms.get(1).getChatRoom());
    }


    @DisplayName("ID로 ChatRoom 찾기 테스트")
    @Test
    public void testFindById() {
        // given
        String roomId = "testRoomId";
        ChatRoom chatRoom = new ChatRoom(shareBoard);
        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.of(chatRoom));

        // when
        ChatRoom result = chatRoomService.findById(roomId);

        // then
        assertNotNull(result);
        verify(chatRoomRepository, times(1)).findByRoomId(roomId);

        // 추가로 값 확인
        assertEquals(chatRoom, result);
    }


    @DisplayName("ID로 ChatRoom 찾기 실패 테스트")
    @Test
    public void testFindById_notFound() {
        // given
        String roomId = "testRoomId";
        when(chatRoomRepository.findByRoomId(roomId)).thenReturn(Optional.empty());

        // when and then
        CustomException exception = assertThrows(CustomException.class,
                () -> chatRoomService.findById(roomId));
        assertEquals(exception.getErrorCode(), ErrorCode.NOT_FOUND_CHATROOM);
    }

}
