CREATE TABLE IF NOT EXISTS users (
    userSeq BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS questions (
    questionSeq BIGINT AUTO_INCREMENT PRIMARY KEY,
    userSeq BIGINT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    title VARCHAR(200) NOT NULL,
    contents TEXT NOT NULL,
    FOREIGN KEY (userSeq) REFERENCES users(userSeq)
);

CREATE TABLE IF NOT EXISTS comments (
    commentSeq BIGINT AUTO_INCREMENT PRIMARY KEY,
    userSeq BIGINT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    questionSeq BIGINT NOT NULL,
    contents TEXT NOT NULL,
    FOREIGN KEY (userSeq) REFERENCES users(userSeq),
    FOREIGN KEY (questionSeq) REFERENCES questions(questionSeq)
);

