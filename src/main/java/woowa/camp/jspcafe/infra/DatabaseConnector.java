package woowa.camp.jspcafe.infra;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnector {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnector.class);
    private static final String CONFIG_FILE_PATH = "/configuration/datasource.properties";
    private DataSource dataSource;

    public DatabaseConnector() {
        this(null);
    }

    public DatabaseConnector(ServletContext servletContext) {
        loadConfig(servletContext);
    }

    private void loadConfig(ServletContext servletContext) {
        log.debug("Database Connecting start");
        Properties prop = new Properties();
        try (InputStream inputStream = getConfigInputStream(servletContext)) {
            if (inputStream == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE_PATH);
            }
            prop.load(inputStream);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(prop.getProperty("datasource.url"));
            hikariConfig.setUsername(prop.getProperty("datasource.username"));
            hikariConfig.setPassword(prop.getProperty("datasource.password"));
            hikariConfig.setDriverClassName(prop.getProperty("datasource.driver-class-name"));

            this.dataSource = new HikariDataSource(hikariConfig);
            log.debug("Database Connecting success");
        } catch (IOException e) {
            throw new RuntimeException("Database configuration could not be loaded", e);
        }

    }

    private InputStream getConfigInputStream(ServletContext servletContext) {
        if (servletContext != null) {
            return servletContext.getResourceAsStream("/WEB-INF/classes" + CONFIG_FILE_PATH);
        }
        return getClass().getResourceAsStream(CONFIG_FILE_PATH);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
