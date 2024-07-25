DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       nickname VARCHAR(50),
                       email VARCHAR(100) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS Question;

CREATE TABLE Question (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          writer VARCHAR(50) NOT NULL,
                          title VARCHAR(200) NOT NULL,
                          contents TEXT NOT NULL,
                          date VARCHAR(20) NOT NULL
);