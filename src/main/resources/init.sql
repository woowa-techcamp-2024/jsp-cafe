-- 기존 테이블 삭제
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS Users;

-- Users 테이블 생성
CREATE TABLE Users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50),
                       email VARCHAR(100) NOT NULL UNIQUE
);

-- Question 테이블 생성 (user_id 외래 키 및 date 필드 변경)
CREATE TABLE Question (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(200) NOT NULL,
                          contents TEXT NOT NULL,
                          date TIMESTAMP NOT NULL, -- date 필드를 TIMESTAMP로 변경
                          user_id BIGINT, -- 외래 키 컬럼 추가
                          FOREIGN KEY (user_id) REFERENCES Users(id) -- 외래 키 설정
);

-- 예제 데이터 삽입
INSERT INTO Users (user_id, password, nickname, email)
VALUES ('seungsu', '994499', '김승수', 'seungsu123@naver.com');
