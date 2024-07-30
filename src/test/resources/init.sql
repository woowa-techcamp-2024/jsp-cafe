-- 기존 테이블이 존재한다면 삭제
DROP TABLE IF EXISTS ARTICLE;
DROP TABLE IF EXISTS MEMBER;

-- member 테이블 생성
CREATE TABLE MEMBER (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        memberId VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
);

-- article 테이블 생성
CREATE TABLE ARTICLE (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         writer VARCHAR(255) NOT NULL,
                         contents TEXT NOT NULL
);
