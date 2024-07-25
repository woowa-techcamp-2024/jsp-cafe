package com.woowa.hyeonsik.server.database;

import com.woowa.hyeonsik.server.database.property.DatabaseProperty;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DataSourceManager {
    private final DatabaseProperty databaseProperty;

    public DataSourceManager(DatabaseProperty databaseProperty) {
        this.databaseProperty = databaseProperty;
    }

    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(databaseProperty.getDriverName());
        config.setJdbcUrl(databaseProperty.getUrl());
        config.setUsername(databaseProperty.getUser());
        config.setPassword(databaseProperty.getPassword());

        config.setConnectionTimeout(50000);  // 커넥션을 5초안에 얻지 못하면 예외
        config.setLeakDetectionThreshold(1000);  // 1초 이상 점유하는 커넥션을 로깅
        return new HikariDataSource(config);
    }
}
