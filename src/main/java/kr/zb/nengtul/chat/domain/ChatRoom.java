package kr.zb.nengtul.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "chatRoomWithChatList", attributeNodes = @NamedAttributeNode("chatList"))
public class ChatRoom {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ConnectedChatRoom> connectedChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Chat> chatList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "share_board_id")
    private ShareBoard shareBoard;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public ChatRoom() {
        this.roomId = UUID.randomUUID().toString();
    }

}