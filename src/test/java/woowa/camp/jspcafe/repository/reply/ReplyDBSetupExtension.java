package woowa.camp.jspcafe.repository.reply;

import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class ReplyDBSetupExtension implements BeforeEachCallback, AfterEachCallback {

    private final DatabaseConnector connector;

    public ReplyDBSetupExtension() {
        this.connector = new DatabaseConnector();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        createTables();
    }

    private void createTables() {
        try (Connection connection = connector.getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS replies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        dropTables();
    }

    private void dropTables() {
        try (var connection = connector.getConnection();
             var statement = connection.createStatement()) {

            String dropTableSQL = "DROP TABLE IF EXISTS replies";
            statement.execute(dropTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to drop tables", e);
        }
    }
}
