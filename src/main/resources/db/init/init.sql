CREATE SCHEMA IF NOT EXISTS jsp_cafe;

CREATE TABLE IF NOT EXISTS jsp_cafe.users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing ID, primary key
    user_id   VARCHAR(50)  NOT NULL UNIQUE,      -- Unique user ID
    password VARCHAR(255) NOT NULL,             -- Password field
    name     VARCHAR(100) NOT NULL,             -- User's name
    email    VARCHAR(100) NOT NULL UNIQUE       -- User's email, must be unique
    );

CREATE TABLE IF NOT EXISTS jsp_cafe.articles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    writer     VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );