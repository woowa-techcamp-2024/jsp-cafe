DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS replies;

CREATE TABLE users
(
    id       VARCHAR(36) PRIMARY KEY,
    email    VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE articles
(
    id         VARCHAR(36) PRIMARY KEY,
    user_id    VARCHAR(36)  NOT NULL,
    title      VARCHAR(200) NOT NULL,
    nickname   VARCHAR(50),
    content    CLOB         NOT NULL,
    create_at  TIMESTAMP    NOT NULL,
    update_at  TIMESTAMP    NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE replies
(
    id         VARCHAR(36) PRIMARY KEY,
    article_id VARCHAR(36) NOT NULL,
    user_id    VARCHAR(36) NOT NULL,
    nickname   VARCHAR(50) NOT NULL,
    content    CLOB        NOT NULL,
    create_at  TIMESTAMP   NOT NULL,
    update_at  TIMESTAMP   NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE INDEX idx_articles_nickname ON articles (nickname);
CREATE INDEX idx_articles_create_at ON articles (update_at);
