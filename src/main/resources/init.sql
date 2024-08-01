-- 기존 테이블 삭제
DROP TABLE IF EXISTS Reply;
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
                          date TIMESTAMP NOT NULL,
                          user_id BIGINT,
                          FOREIGN KEY (user_id) REFERENCES Users(id)
);

-- Reply 테이블 생성
CREATE TABLE Reply (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id BIGINT,
                       question_id BIGINT,
                       contents TEXT NOT NULL,
                       date TIMESTAMP NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES Users(id),
                       FOREIGN KEY (question_id) REFERENCES Question(id)
);

-- 예제 데이터 삽입
-- Users 데이터
INSERT INTO Users (user_id, password, nickname, email)
VALUES ('seungsu', '994499', '김승수', 'seungsu123@naver.com'),
       ('user2', 'password2', '닉네임2', 'user2@example.com'),
       ('user3', 'password3', '닉네임3', 'user3@example.com');

-- Question 데이터
INSERT INTO Question (title, contents, date, user_id)
VALUES ('첫 번째 질문', '첫 번째 질문 내용', CURRENT_TIMESTAMP, 1),
       ('두 번째 질문', '두 번째 질문 내용', CURRENT_TIMESTAMP, 2),
       ('세 번째 질문', '세 번째 질문 내용', CURRENT_TIMESTAMP, 3);

-- Reply 데이터
INSERT INTO Reply (user_id, question_id, contents, date)
VALUES (1, 1, '첫 번째 질문에 대한 첫 번째 댓글', CURRENT_TIMESTAMP),
       (2, 1, '첫 번째 질문에 대한 두 번째 댓글', CURRENT_TIMESTAMP),
       (3, 1, '첫 번째 질문에 대한 세 번째 댓글', CURRENT_TIMESTAMP),
       (1, 2, '두 번째 질문에 대한 첫 번째 댓글', CURRENT_TIMESTAMP),
       (2, 2, '두 번째 질문에 대한 두 번째 댓글', CURRENT_TIMESTAMP),
       (3, 2, '두 번째 질문에 대한 세 번째 댓글', CURRENT_TIMESTAMP),
       (1, 3, '세 번째 질문에 대한 첫 번째 댓글', CURRENT_TIMESTAMP),
       (2, 3, '세 번째 질문에 대한 두 번째 댓글', CURRENT_TIMESTAMP),
       (3, 3, '세 번째 질문에 대한 세 번째 댓글', CURRENT_TIMESTAMP);
