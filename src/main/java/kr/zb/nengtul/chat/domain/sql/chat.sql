CREATE TABLE chat_room (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           roomId VARCHAR(255),
                           createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE connected_chat_room (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     room_id BIGINT,
                                     user_id BIGINT,
                                     FOREIGN KEY (room_id) REFERENCES chat_room(id),
                                     FOREIGN KEY (user_id) REFERENCES user(id)
);
CREATE TABLE chat (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      chat_room_id BIGINT,
                      sender_id BIGINT,
                      content TEXT,
                      `read` BOOLEAN,
                      createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (chat_room_id) REFERENCES chat_room(id),
                      FOREIGN KEY (sender_id) REFERENCES user(id)
);
