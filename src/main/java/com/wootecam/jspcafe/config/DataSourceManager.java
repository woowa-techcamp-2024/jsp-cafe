package com.wootecam.jspcafe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceManager {

    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);

    private final DataSource dataSource;

    public DataSourceManager(final DataSourceProperty dataSourceProperty) {
        String driverClassName = dataSourceProperty.getDriverClassName();
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("드라이버 클래스를 로딩할 수 없습니다.");
        }
        String url = dataSourceProperty.getUrl();
        String user = dataSourceProperty.getUser();
        String password = dataSourceProperty.getPassword();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);

        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void shutdown() {
        if (dataSource != null) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
