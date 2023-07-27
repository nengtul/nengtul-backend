package kr.zb.nengtul.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    @Column(columnDefinition = "json")
    private String userId;
    @OneToMany(mappedBy = "roomId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Chat> chatList = new ArrayList<>();
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public ChatRoom() {
        this.roomId = UUID.randomUUID().toString();
    }

    public Set<Long> getUserId() {
        if (userId == null || userId.isEmpty()) {
            return Collections.emptySet();
        }

        Gson gson = new Gson();
        return gson.fromJson(userId, new TypeToken<Set<Long>>() {
        }.getType());
    }

    public void setUserId(Set<Long> userIdSet) {
        Gson gson = new Gson();
        this.userId = gson.toJson(userIdSet);
    }

}