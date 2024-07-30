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
    title      VARCHAR(255) NOT NULL,
    writer     VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create table jsp_cafe.roles
(
    user_id varchar(15) not null,
    name      varchar(15) not null,
    primary key (user_id, name)
);

INSERT INTO jsp_cafe.users (user_id, password, name, email)
VALUES ('user1', 'password_hash_1', 'John Doe', 'john.doe@example.com'),
       ('user2', 'password_hash_2', 'Jane Smith', 'jane.smith@example.com'),
       ('user3', 'password_hash_3', 'Alice Johnson', 'alice.johnson@example.com');
INSERT INTO jsp_cafe.articles (title, writer, content, created_at)
VALUES ('First Article', 'John Doe', 'This is the content of the first article.', '2024-07-30 10:00:00'),
       ('Second Article', 'Jane Smith', 'This is the content of the second article.', '2024-07-30 11:00:00'),
       ('Third Article', 'Alice Johnson', 'This is the content of the third article.', '2024-07-30 12:00:00');
INSERT INTO jsp_cafe.roles (user_id, name)
VALUES ('user1', 'manager-gui'),
       ('user1', 'ROLE_ADMIN'),
       ('user2', 'ROLE_USER'),
       ('user3', 'ROLE_USER');
