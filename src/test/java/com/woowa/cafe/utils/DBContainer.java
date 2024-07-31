package com.woowa.cafe.utils;

import ch.qos.logback.classic.Level;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import javax.sql.DataSource;

public class DBContainer {

    private static final MySQLContainer<?> mysqlContainer;
    private static final DataSource dataSource;

    static {
        mysqlContainer = new MySQLContainer<>("mysql:8.0.26")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("");

        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ERROR);
        mysqlContainer.withLogConsumer(new Slf4jLogConsumer(rootLogger));
        mysqlContainer.start();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(mysqlContainer.getJdbcUrl());
        hikariConfig.setUsername(mysqlContainer.getUsername());
        hikariConfig.setPassword(mysqlContainer.getPassword());

        dataSource = new HikariDataSource(hikariConfig);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }


}
