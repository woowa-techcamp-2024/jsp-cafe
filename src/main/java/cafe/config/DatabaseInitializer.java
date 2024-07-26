package cafe.config;

import cafe.domain.DatabaseManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DatabaseInitializer implements ServletContextListener {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try (Statement statement = DatabaseManager.connect().createStatement()) {
            InputStream inputStream = DatabaseManager.class.getResourceAsStream("/sql/init.sql");
            String[] sqls = new String(Objects.requireNonNull(inputStream).readAllBytes()).split("\n");
            for (String sql: sqls) {
                statement.execute(sql);
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
