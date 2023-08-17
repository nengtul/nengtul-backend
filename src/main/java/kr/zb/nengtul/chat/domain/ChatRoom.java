package kr.zb.nengtul.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "chatRoomWithShareBoardAndConnectedChatRooms",
        attributeNodes = {
                @NamedAttributeNode("shareBoard"),
                @NamedAttributeNode(value = "connectedChatRooms", subgraph = "connectedChatRoomsWithUser")
        },
        subgraphs = @NamedSubgraph(name = "connectedChatRoomsWithUser", attributeNodes = @NamedAttributeNode("userId")))
@NamedEntityGraph(name = "chatRoomWithShareBoardAndConnectedChatRoomsAndChtList",
        attributeNodes = {
                @NamedAttributeNode("shareBoard"),
                @NamedAttributeNode("chatList"),
                @NamedAttributeNode(value = "connectedChatRooms", subgraph = "connectedChatRoomsWithUser")
        },
        subgraphs = @NamedSubgraph(name = "connectedChatRoomsWithUser", attributeNodes = @NamedAttributeNode("userId")))

public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private String roomId;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private Set<ConnectedChatRoom> connectedChatRooms = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private List<Chat> chatList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "share_board_id")
    private ShareBoard shareBoard;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public ChatRoom(ShareBoard shareBoard) {
        this.roomId = UUID.randomUUID().toString();
        this.shareBoard = shareBoard;
    }

    public void setShareBoard(ShareBoard shareBoard){
        this.shareBoard = shareBoard;
    }

}