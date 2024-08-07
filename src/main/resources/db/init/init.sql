CREATE SCHEMA IF NOT EXISTS jsp_cafe;

CREATE TABLE IF NOT EXISTS jsp_cafe.users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing ID, primary key
    user_id  VARCHAR(20)  NOT NULL UNIQUE,      -- Unique user ID
    password VARCHAR(20) NOT NULL,              -- Password field
    name     VARCHAR(15) NOT NULL,              -- User's name
    email    VARCHAR(50) NOT NULL UNIQUE        -- User's email, must be unique
);

CREATE TABLE IF NOT EXISTS jsp_cafe.articles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(100)                  NOT NULL,
    writer     VARCHAR(20)                  NOT NULL,
    content    TEXT                         NOT NULL,
    status     ENUM ('PUBLISHED', 'DELETED') NOT NULL DEFAULT 'PUBLISHED',
    created_at TIMESTAMP                              DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (writer) REFERENCES jsp_cafe.users(user_id)
);

CREATE TABLE IF NOT EXISTS jsp_cafe.comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT                        NOT NULL,
    writer     VARCHAR(20)                   NOT NULL,
    content    TEXT                          NOT NULL,
    status     ENUM ('COMMENTED', 'DELETED') NOT NULL DEFAULT 'COMMENTED',
    created_at TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES jsp_cafe.articles(id),
    FOREIGN KEY (writer) REFERENCES jsp_cafe.users(user_id)
);

CREATE TABLE IF NOT EXISTS jsp_cafe.roles
(
    user_id VARCHAR(20) NOT NULL,
    name    VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, name)
);

