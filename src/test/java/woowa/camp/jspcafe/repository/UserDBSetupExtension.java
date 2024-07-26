package woowa.camp.jspcafe.repository;

import java.sql.Connection;
import java.sql.Statement;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class UserDBSetupExtension implements BeforeEachCallback, AfterEachCallback {

    private final DatabaseConnector connector;

    public UserDBSetupExtension() {
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

    private void dropTables() {
        try (var connection = connector.getConnection();
             var statement = connection.createStatement()) {

            String dropTableSQL = "DROP TABLE IF EXISTS users";
            statement.execute(dropTableSQL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to drop tables", e);
        }
    }

}
