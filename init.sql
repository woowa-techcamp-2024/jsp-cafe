-- 기존 테이블 제거
USE mydb;

SET GLOBAL local_infile = 1;

DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS replies;

-- Article 테이블 생성
CREATE TABLE IF NOT EXISTS articles (
    article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author VARCHAR(100) NOT NULL,
    alive_status VARCHAR(20) NOT NULL,
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

CREATE TABLE IF NOT EXISTS replies(
    reply_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    author VARCHAR(100) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    alive_status VARCHAR(20) NOT NULL,
    created_dt DATETIME NOT NULL
);

-- Load data from CSV file
LOAD DATA INFILE '/var/lib/mysql-files/articles.csv'
INTO TABLE articles
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(article_id, user_id, title, content, author, alive_status, created_dt);

LOAD DATA INFILE '/var/lib/mysql-files/users.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(user_id, email, nickname, password, created_dt);

LOAD DATA INFILE '/var/lib/mysql-files/replies.csv'
INTO TABLE replies
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(reply_id, user_id, article_id, author, comment, alive_status, created_dt);
--