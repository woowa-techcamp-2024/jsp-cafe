DELIMITER //

CREATE PROCEDURE InsertMockData()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE j INT DEFAULT 0;
    DECLARE user_id INT;
    DECLARE post_id INT;
    DECLARE comment_count INT;

    -- 사용자 10,000명 등록
    WHILE i < 10000 DO
            INSERT INTO users (user_id, email, password, name, created_at)
            VALUES (
                       CONCAT('user', i),
                       CONCAT('user', i, '@example.com'),
                       CONCAT('password', i),
                       CONCAT('Name ', i),
                       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
                   );
            SET i = i + 1;
        END WHILE;

    -- 게시글 50만개 작성
    SET i = 0;
    WHILE i < 500000 DO
            SET user_id = FLOOR(1 + RAND() * 10000);
            INSERT INTO posts (writer_id, title, contents, created_at)
            VALUES (
                       user_id,
                       CONCAT('Title ', i),
                       CONCAT('Content ', i, ' ', REPEAT('Lorem ipsum ', 20)),
                       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
                   );
            SET post_id = LAST_INSERT_ID();

            -- 각 게시글에 0-10개의 댓글 추가
            SET comment_count = FLOOR(RAND() * 11);
            SET j = 0;
            WHILE j < comment_count DO
                    INSERT INTO comments (post_id, writer_id, contents, created_at)
                    VALUES (
                               post_id,
                               FLOOR(1 + RAND() * 10000),
                               CONCAT('Comment ', j, ' on post ', i),
                               DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
                           );
                    SET j = j + 1;
                END WHILE;

            SET i = i + 1;
            IF MOD(i, 1000) = 0 THEN
                SELECT CONCAT(i, ' posts inserted') AS Progress;
            END IF;
        END WHILE;

    SELECT 'Data insertion completed' AS Result;
END //

DELIMITER ;

CALL InsertMockData();