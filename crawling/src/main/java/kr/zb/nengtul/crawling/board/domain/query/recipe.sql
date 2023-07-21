CREATE TABLE recipe
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id           BIGINT      NOT NULL,
    title             VARCHAR(20) NOT NULL,
    intro             TEXT(100)   NOT NULL,
    ingredient        VARCHAR(50) NOT NULL,
    cooking_step      TEXT(100)   NOT NULL,
    image_url         TEXT(100)   NOT NULL,
    cooking_time      VARCHAR(10) DEFAULT '-',
    serving           VARCHAR(10) DEFAULT '-',
    category          VARCHAR(50) DEFAULT '-',
    video_url         VARCHAR(50) DEFAULT '-',
    view_count        BIGINT      DEFAULT 0,
    creation_date     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    modification_date TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
