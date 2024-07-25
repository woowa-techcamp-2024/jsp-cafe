package com.wootecam.jspcafe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);

    private static DataSource dataSource;

    public DataSourceManager() {
        if (dataSource == null) {
            Properties props = new Properties();
            loadProperties(props);
            String url = props.getProperty("datasource.url");
            String user = props.getProperty("datasource.user");
            String password = props.getProperty("datasource.password");

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);

            dataSource = new HikariDataSource(hikariConfig);
        }
    }

    private void loadProperties(final Properties props) {
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("config/datasource.properties"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
