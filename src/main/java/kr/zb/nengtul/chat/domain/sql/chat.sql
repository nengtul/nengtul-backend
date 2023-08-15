CREATE TABLE chat_room
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_Id         VARCHAR(255),
    share_board_id BIGINT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (share_board_id) REFERENCES share_board (id)
);
CREATE TABLE connected_chat_room
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT,
    user_id BIGINT,
    leave_room BOOLEAN,

    FOREIGN KEY (room_id) REFERENCES chat_room (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);
CREATE TABLE chat
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT,
    sender_id    BIGINT,
    content      TEXT,
    read_mark       BOOLEAN,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_room_id) REFERENCES chat_room (id),
    FOREIGN KEY (sender_id) REFERENCES user (id)
);
