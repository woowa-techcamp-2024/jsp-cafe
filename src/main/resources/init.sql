CREATE TABLE IF NOT EXISTS `user`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `username`   varchar(50)  NOT NULL,
    `password`   varchar(300) NOT NULL,
    `name`       varchar(100) NOT NULL,
    `email`      varchar(100) NOT NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    `deleted`    tinyint(1)   NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_username_idx` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `post`
(
    `id`         bigint         NOT NULL AUTO_INCREMENT,
    `author_id`  bigint         NOT NULL,
    `title`      varchar(255)   NOT NULL,
    `content`    varchar(10000) NOT NULL,
    `filename`   varchar(255)            DEFAULT NULL,
    `view`       int            NOT NULL DEFAULT '0',
    `created_at` datetime       NOT NULL,
    `updated_at` datetime       NOT NULL,
    `deleted`    tinyint(1)     NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    CONSTRAINT `powst_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `post_id`    bigint       NOT NULL,
    `parent_id`  bigint DEFAULT NULL,
    `user_id`    bigint       NOT NULL,
    `content`    varchar(300) NOT NULL,
    `created_at` datetime     NOT NULL,
    `updated_at` datetime     NOT NULL,
    `deleted`    tinyint      NOT NULL,
    PRIMARY KEY (`id`),
    KEY `post_id` (`post_id`),
    KEY `parent_id` (`parent_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
    CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `comment_ibfk_4` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
# REPLACE INTO `user` (id, username, password, name, email, created_at, updated_at)
# VALUES (1, "woowa2024", "woowa1234", "kimwoowa", "woowa@gmail.com", now(), now());
# REPLACE INTO `post` (id, author_id, title, content, view, created_at, updated_at)
# VALUES (1, 1, 'First Post', 'This is the content of the first post.', 10, '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
#        (2, 1, 'Second Post', 'This is the content of the second post.', 20, '2024-01-02 11:00:00',
#         '2024-01-03 10:00:00'),
#        (3, 1, 'Third Post', 'This is the content of the third post.', 30, '2024-01-03 12:00:00', '2024-01-04 10:00:00'),
#        (4, 1, 'Fourth Post', 'This is the content of the fourth post.', 40, '2024-01-04 13:00:00',
#         '2024-01-05 10:00:00'),
#        (5, 1, 'Fifth Post', 'This is the content of the fifth post.', 50, '2024-01-05 14:00:00', '2024-01-06 10:00:00'),
#        (6, 1, 'Sixth Post', 'This is the content of the sixth post.', 60, '2024-01-06 15:00:00', '2024-01-07 10:00:00'),
#        (7, 1, 'Seventh Post', 'This is the content of the seventh post.', 70, '2024-01-07 16:00:00',
#         '2024-01-08 10:00:00'),
#        (8, 1, 'Eighth Post', 'This is the content of the eighth post.', 80, '2024-01-08 17:00:00',
#         '2024-01-09 10:00:00'),
#        (9, 1, 'Ninth Post', 'This is the content of the ninth post.', 90, '2024-01-09 18:00:00', '2024-01-10 10:00:00'),
#        (10, 1, 'Tenth Post', 'This is the content of the tenth post.', 100, '2024-01-10 19:00:00',
#         '224-01-11 10:00:00');
#

# REPLACE INTO `comment` (`id`, `post_id`, `parent_id`, `user_id`, `content`, `created_at`, `updated_at`, `deleted`)  VALUES
#(1, 1, NULL, 1, '좋은 글 감사합니다', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0),
#(2, 1, 1, 2, '좋아여', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0),
#(3, 1, 2, 12, '넵', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0),
#(4, 1, NULL, 1, '호호', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0),
#(5, 1, 4, 12, '좋은 밤 되세요', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0),
#(6, 1, NULL, 13, '화이팅', '2024-08-01 23:23:07', '2024-08-01 23:23:07', 0);
