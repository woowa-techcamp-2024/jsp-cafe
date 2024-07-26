package org.example.jspcafe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractRepositoryTestSupport {

    protected DatabaseConnectionManager connectionManager = new H2DatabaseConnectionManager();
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = connectionManager.getConnection();
        createTables();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        deleteAllInBatch();
        if (connection != null) {
            connection.close();
        }
    }

    protected abstract void deleteAllInBatch();

    private void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "nickname VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "created_at TIMESTAMP NOT NULL" +
                    ");";

            String createPostsTable = "CREATE TABLE IF NOT EXISTS posts (" +
                    "post_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id BIGINT NOT NULL, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "content VARCHAR(255) NOT NULL, " +
                    "created_at TIMESTAMP NOT NULL" +
                    ");";

            statement.execute(createUsersTable);
            statement.execute(createPostsTable);
        }
    }
}
