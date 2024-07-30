-- 유저 테이블 삭제 (이미 존재하는 경우)
DROP TABLE IF EXISTS users;

-- 유저 테이블 생성
CREATE TABLE users(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  VARCHAR(255),
    password VARCHAR(255),
    nickname     VARCHAR(255),
    email    VARCHAR(255)
);

-- 아티클 테이블 삭제 (이미 존재하는 경우)
DROP TABLE IF EXISTS articles;

-- 아티클 테이블 생성
CREATE TABLE articles(
    article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT NOT NULL,
    author     VARCHAR(255) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);
