package woopaca.jspcafe.database;

public abstract class MySQLConstants {

    public static final String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS `user` (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            username VARCHAR(40) NOT NULL,
            nickname VARCHAR(20) NOT NULL,
            password VARCHAR(20) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;
    public static final String CREATE_POST_TABLE = """
            CREATE TABLE IF NOT EXISTS `post` (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            title VARCHAR(30) NOT NULL,
            content VARCHAR(1000) NOT NULL,
            view_count INT NOT NULL DEFAULT 0,
            written_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            writer_id BIGINT NOT NULL,
            status ENUM('PUBLISHED', 'DELETED') DEFAULT 'PUBLISHED'
            );
            """;
    public static final String CREATE_REPLY_TABLE = """
            CREATE TABLE IF NOT EXISTS `reply` (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            content VARCHAR(200) NOT NULL,
            written_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            writer_id BIGINT NOT NULL,
            post_id BIGINT NOT NULL,
            status ENUM('PUBLISHED', 'DELETED') DEFAULT 'PUBLISHED'
            );
            """;
}
