CREATE SCHEMA IF NOT EXISTS jsp_cafe;

CREATE TABLE IF NOT EXISTS jsp_cafe.users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY, -- Auto-incrementing ID, primary key
    user_id  VARCHAR(50)  NOT NULL UNIQUE,      -- Unique user ID
    password VARCHAR(255) NOT NULL,             -- Password field
    name     VARCHAR(100) NOT NULL,             -- User's name
    email    VARCHAR(100) NOT NULL UNIQUE       -- User's email, must be unique
);

CREATE TABLE IF NOT EXISTS jsp_cafe.articles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255)                  NOT NULL,
    writer     VARCHAR(255)                  NOT NULL,
    content    TEXT                          NOT NULL,
    status     ENUM ('PUBLISHED', 'DELETED') NOT NULL DEFAULT 'PUBLISHED',
    created_at TIMESTAMP                              DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS jsp_cafe.comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT                        NOT NULL,
    writer     VARCHAR(255)                  NOT NULL,
    content    TEXT                          NOT NULL,
    status     ENUM ('COMMENTED', 'DELETED') NOT NULL DEFAULT 'COMMENTED',
    created_at TIMESTAMP                     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES jsp_cafe.articles(id),
    FOREIGN KEY (writer) REFERENCES jsp_cafe.users(user_id)
);

create table jsp_cafe.roles
(
    user_id varchar(15) not null,
    name    varchar(15) not null,
    primary key (user_id, name)
);

INSERT INTO jsp_cafe.users (user_id, password, name, email)
VALUES ('user1', 'password_hash_1', 'John Doe', 'john.doe@example.com'),
       ('user2', 'password_hash_2', 'Jane Smith', 'jane.smith@example.com'),
       ('user3', 'password_hash_3', 'Alice Johnson', 'alice.johnson@example.com');
INSERT INTO jsp_cafe.articles (title, writer, content, status, created_at)
VALUES ('First Article', 'John Doe', 'This is the content of the first article.', 'PUBLISHED', '2024-07-30 10:00:00'),
       ('Second Article', 'Jane Smith', 'This is the content of the second article.', 'DELETED', '2024-07-30 11:00:00'),
       ('Third Article', 'Alice Johnson', 'This is the content of the third article.', 'PUBLISHED',
        '2024-07-30 12:00:00');
INSERT INTO jsp_cafe.comments (article_id, writer, content, status, created_at)
VALUES (1, 'John Doe', 'This is a comment on the first article.', 'COMMENTED', '2024-07-30 13:00:00'),
       (2, 'Jane Smith', 'This is a comment on the second article.', 'COMMENTED', '2024-07-30 14:00:00'),
       (3, 'Alice Johnson', 'This is a comment on the third article.', 'COMMENTED', '2024-07-30 15:00:00');
INSERT INTO jsp_cafe.roles (user_id, name)
VALUES ('user1', 'manager-gui'),
       ('user1', 'ROLE_ADMIN'),
       ('user2', 'ROLE_USER'),
       ('user3', 'ROLE_USER');