package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Init implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String sqlScript = """
            -- 유저 테이블 삭제 (이미 존재하는 경우)
            DROP TABLE IF EXISTS users;
            
            -- 유저 테이블 생성
            CREATE TABLE users (
                user_id  VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255),
                name     VARCHAR(255),
                email    VARCHAR(255)
            );
            
            -- 초기 유저 데이터 삽입
            INSERT INTO users (user_id, password, name, email) VALUES
                ('a', 'a', 'a', 'a');
            
            -- 아티클 테이블 삭제 (이미 존재하는 경우)
            DROP TABLE IF EXISTS articles;
            
            -- 아티클 테이블 생성
            CREATE TABLE articles (
                article_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title      VARCHAR(255) NOT NULL,
                content    TEXT NOT NULL
            );
            """;

        DatabaseManager.executeSqlScript(sqlScript);
    }

}
