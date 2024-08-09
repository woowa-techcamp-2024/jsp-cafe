CREATE TABLE IF NOT EXISTS `users`
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    VARCHAR(20)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    `name`     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS posts
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    writer_id  BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    contents   TEXT         NOT NULL,
    is_present BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL,
    INDEX idx_is_present (is_present, created_at)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id    BIGINT    NOT NULL,
    writer_id  BIGINT    NOT NULL,
    contents   TEXT      NOT NULL,
    is_present BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    INDEX idx_post_id (post_id, is_present, created_at)
);