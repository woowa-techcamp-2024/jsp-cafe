CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    username VARCHAR(255)        NOT NULL,
    email    VARCHAR(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS articles
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    writer    VARCHAR(255) NOT NULL,
    contents  TEXT         NOT NULL,
    createdAt DATETIME
);