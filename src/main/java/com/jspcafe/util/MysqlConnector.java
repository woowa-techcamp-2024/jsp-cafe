package com.jspcafe.util;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlConnector implements DatabaseConnector {

  private static final String CONFIG_FILE = "config.properties";

  private static MysqlConnectionPoolDataSource dataSource;

  static {
    try {
      Properties props = new Properties();
      InputStream inputStream = MysqlConnector.class.getClassLoader()
          .getResourceAsStream(CONFIG_FILE);
      if (inputStream == null) {
        throw new IOException("Unable to find " + CONFIG_FILE);
      }
      props.load(inputStream);

      dataSource = new MysqlConnectionPoolDataSource();
      dataSource.setURL(props.getProperty("db.url"));
      dataSource.setUser(props.getProperty("db.user"));
      dataSource.setPassword(props.getProperty("db.password"));

      dataSource.setMaxReconnects(5);
      dataSource.setInitialTimeout(2);
      dataSource.setAutoReconnect(true);
    } catch (SQLException | IOException e) {
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
