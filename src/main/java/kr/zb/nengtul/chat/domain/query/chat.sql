CREATE TABLE chat_room (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           room_id VARCHAR(255) NOT NULL,
                           user_id JSON,
                           created_at DATETIME NOT NULL
);

CREATE TABLE chat (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      room_id BIGINT,
                      sender BIGINT,
                      content TEXT,
                      created_at DATETIME NOT NULL,
                      FOREIGN KEY (room_id) REFERENCES chat_room(id)
);

