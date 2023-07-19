CREATE TABLE recipe (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT,
                        title VARCHAR(20),
                        intro TEXT(100),
                        ingredient VARCHAR(50),
                        cooking_step TEXT(100),
                        image_url TEXT(100),
                        cooking_time VARCHAR(10),
                        serving VARCHAR(10),
                        category VARCHAR(50) DEFAULT '-',
                        video_url VARCHAR(50),
                        view_count BIGINT DEFAULT 0,
                        creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
