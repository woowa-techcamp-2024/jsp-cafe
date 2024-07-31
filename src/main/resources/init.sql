USE jspcafe;

CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS articles (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    title VARCHAR(200) NOT NULL,
    nickname VARCHAR(50),
    content TEXT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS replies (
    id VARCHAR(36) PRIMARY KEY,
    article_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    create_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP
);

INSERT INTO users (id, email, nickname, password)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'user1@example.com', 'User1', 'hashed_password_1'),
    ('550e8400-e29b-41d4-a716-446655440001', 'user2@example.com', 'User2', 'hashed_password_2'),
    ('550e8400-e29b-41d4-a716-446655440002', 'user3@example.com', 'User3', 'hashed_password_3');

INSERT INTO articles (id, user_id, title, nickname, content, create_at, update_at, is_deleted, deleted_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'First Article', 'User1', 'This is the content of the first article.', '2023-07-24 10:00:00', '2023-07-24 10:00:00', FALSE, NULL),
    ('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440001', 'Second Article', 'User2', 'This is the content of the second article.', '2023-07-24 11:00:00', '2023-07-24 11:00:00', FALSE, NULL),
    ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440002', 'Third Article', 'User3', 'This is the content of the third article.', '2023-07-24 12:00:00', '2023-07-24 12:00:00', FALSE, NULL);

INSERT INTO replies (id, article_id, user_id, nickname, content, create_at, update_at, is_deleted, deleted_at)
VALUES
    ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'User1', 'This is a reply to the first article.', '2023-07-24 10:30:00', '2023-07-24 10:30:00', FALSE, NULL),
    ('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', 'User2', 'I agree with the first reply!', '2023-07-24 11:00:00', '2023-07-24 11:00:00', FALSE, NULL),
    ('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'User3', 'Interesting article, thanks for sharing!', '2023-07-24 12:30:00', '2023-07-24 12:30:00', FALSE, NULL);