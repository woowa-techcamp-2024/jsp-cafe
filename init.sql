-- 기존 테이블 제거
use mydb;

DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;

-- Article 테이블 생성
CREATE TABLE IF NOT EXISTS articles (
    article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100) NOT NULL,
    created_dt DATETIME NOT NULL
);

-- User 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_dt DATETIME NOT NULL
);