CREATE TABLE recipe
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id           BIGINT      NOT NULL,
    title             VARCHAR(255) NOT NULL,
    intro             VARCHAR(1000)   NOT NULL,
    ingredient        TEXT(100) NOT NULL,
    cooking_step      TEXT(100)   NOT NULL,
    image_url         TEXT(100)   NOT NULL,
    cooking_time      VARCHAR(10) DEFAULT '',
    serving           VARCHAR(10) DEFAULT '',
    category          VARCHAR(50) NOT NULL ,
    video_url         VARCHAR(2000) DEFAULT '',
    main_photo_url         VARCHAR(2000) NOT NULL,
    view_count        BIGINT      DEFAULT 0,
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
