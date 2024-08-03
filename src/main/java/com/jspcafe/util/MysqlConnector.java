package com.jspcafe.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlConnector implements DatabaseConnector {

  private static final String CONFIG_FILE = "config.properties";
  private static HikariDataSource dataSource;

  static {
    try {
      Properties props = new Properties();
      InputStream inputStream = MysqlConnector.class.getClassLoader()
          .getResourceAsStream(CONFIG_FILE);
      if (inputStream == null) {
        throw new IOException("Unable to find " + CONFIG_FILE);
      }
      props.load(inputStream);

      HikariConfig config = new HikariConfig();
      config.setDriverClassName("com.mysql.cj.jdbc.Driver");
      config.setJdbcUrl(props.getProperty("db.url"));
      config.setUsername(props.getProperty("db.user"));
      config.setPassword(props.getProperty("db.password"));

      // HikariCP 특정 설정
      config.setMaximumPoolSize(10);
      config.setMinimumIdle(10);
      config.setIdleTimeout(300000);
      config.setConnectionTimeout(10000);
      config.setAutoCommit(true);

      // MySQL 특정 설정
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.addDataSourceProperty("useServerPrepStmts", "true");

      dataSource = new HikariDataSource(config);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
