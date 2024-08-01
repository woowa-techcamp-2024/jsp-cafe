package woowa.camp.jspcafe.configuration;

import java.sql.Connection;
import java.sql.Statement;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class InMemoryDBInitializer {

    public static void createArticleTable(DatabaseConnector connector) {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS articles (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        author_id BIGINT,
                        title VARCHAR(255) NOT NULL,
                        content VARCHAR(5000) NOT NULL,
                        hits INT DEFAULT 0,
                        created_at DATE NOT NULL,
                        updated_at DATE
                    )
                    """;

            statement.execute(createTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    public static void createUserTable(DatabaseConnector connector) {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL,
                        nickname VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        register_at DATE NOT NULL
                    )
                    """;

            statement.execute(createTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    public static void createReplyTable(DatabaseConnector connector) {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS replies (
                        reply_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT,
                        article_id BIGINT,
                        content VARCHAR(255),
                        created_at DATETIME,
                        updated_at DATETIME,
                        deleted_at DATETIME
                    )
                    """;

            statement.execute(createTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }
}
