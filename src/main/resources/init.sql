-- cafe 데이터베이스가 없으면 생성
CREATE DATABASE IF NOT EXISTS cafe;

-- cafe 데이터베이스 사용
USE cafe;
-- 기존 테이블이 존재한다면 삭제
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS member;

-- member 테이블 생성
CREATE TABLE member (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        memberId VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
);

-- article 테이블 생성
CREATE TABLE article (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         writer BIGINT NOT NULL,
                         contents TEXT NOT NULL
);