package com.woowa.cafe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;

import javax.sql.DataSource;

public class DataSourceConfig {

    private final DataSource dataSource;

    public DataSourceConfig(ServletContext servletContext) {
        String jdbcUrl = servletContext.getInitParameter("jdbcUrl");
        String dbUsername = servletContext.getInitParameter("dbUsername");

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(5);

        this.dataSource = new HikariDataSource(config);

        createTable(dataSource);
    }

    public static void createTable(final DataSource dataSource) {
        new Thread(() -> {
            String dropMemberTable = "DROP TABLE IF EXISTS members";
            String createMemberTable = "CREATE TABLE IF NOT EXISTS members (" +
                    "member_id VARCHAR(255) PRIMARY KEY, " +
                    "password VARCHAR(255), " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255))";

            String dropArticleTable = "DROP TABLE IF EXISTS articles";
            String createArticleTable = "CREATE TABLE IF NOT EXISTS articles (" +
                    "article_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "writer_id VARCHAR(255), " +
                    "title VARCHAR(255), " +
                    "contents TEXT, " +
                    "reply_count BIGINT, " +
                    "is_deleted BOOLEAN, " +
                    "created_at TIMESTAMP, " +
                    "modified_at TIMESTAMP, " +
                    "INDEX idx_article_is_deleted (is_deleted), " +
                    "INDEX articles_created_at_index (created_at), " +
                    "INDEX articles_modified_at_index (modified_at), " +
                    "INDEX articles_reply_count_index (reply_count), " +
                    "INDEX articles_title_index (title), " +
                    "INDEX articles_writer_id_index (writer_id));";

            String dropReplyTable = "DROP TABLE IF EXISTS replies";
            String createReplyTable = "CREATE TABLE IF NOT EXISTS replies (" +
                    "reply_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "article_id BIGINT, " +
                    "writer_id VARCHAR(255), " +
                    "contents TEXT, " +
                    "is_deleted BOOLEAN, " +
                    "created_at TIMESTAMP, " +
                    "modified_at TIMESTAMP, " +
                    "index idx_reply_is_deleted (is_deleted), " +
                    "index replies_article_id_index (article_id));";

            try (var connection = dataSource.getConnection()) {
//                connection.prepareStatement(dropMemberTable).execute();
                connection.prepareStatement(createMemberTable).execute();
//                connection.prepareStatement(dropArticleTable).execute();
                connection.prepareStatement(createArticleTable).execute();
//                connection.prepareStatement(dropReplyTable).execute();
                connection.prepareStatement(createReplyTable).execute();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
