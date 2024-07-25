CREATE DATABASE IF NOT EXISTS test;
USE test;

CREATE TABLE IF NOT EXISTS `users` (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(20) NOT NULL,
                                      email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS posts (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    writer_id BIGINT NOT NULL,
                                    title VARCHAR(255) NOT NULL,
    contents TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (writer_id) REFERENCES `users`(id)
    );
