package woowa.camp.jspcafe.repository;

import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class ArticleDBSetupExtension implements BeforeEachCallback, AfterEachCallback {

    private final DatabaseConnector connector;

    public ArticleDBSetupExtension() {
        this.connector = new DatabaseConnector();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        createTables();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        dropTables();
    }

    private void createTables() {
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

    private void dropTables() {
        try (var connection = connector.getConnection();
             var statement = connection.createStatement()) {

            String dropTableSQL = "DROP TABLE IF EXISTS articles";
            statement.execute(dropTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to drop tables", e);
        }
    }
}
