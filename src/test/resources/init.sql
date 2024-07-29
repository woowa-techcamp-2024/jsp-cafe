-- Create a user table
CREATE TABLE IF NOT EXISTS `user`
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(300) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE (username)
);

-- Create a post table
CREATE TABLE IF NOT EXISTS post
(
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    author_id  BIGINT         NOT NULL,
    title      VARCHAR(255)   NOT NULL,
    content    VARCHAR(10000) NOT NULL,
    filename   VARCHAR(255)            DEFAULT NULL,
    view       INT            NOT NULL DEFAULT 0,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL,
    deleted    BOOLEAN        NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (author_id) REFERENCES `user` (id)
);
