package cafe.domain.util;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector implements DatabaseConnector {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cafe";
    private static final String DB_USER = "cafe";
    private static final String DB_PASSWORD = "cafe";
    private static MysqlConnectionPoolDataSource dataSource;

    static {
        try {
            Class.forName(DRIVER);
            dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL(DB_URL);
            dataSource.setUser(DB_USER);
            dataSource.setPassword(DB_PASSWORD);

            dataSource.setMaxReconnects(5);
            dataSource.setInitialTimeout(2);
            dataSource.setAutoReconnect(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public MySQLConnector() {
        init();
    }

    @Override
    public void init() {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sql/init.sql");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                throw new RuntimeException("SQL script not found");
            }

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                sql.append(line).append("\n");
            }

            String[] sqls = sql.toString().split(";");
            for (String s: sqls) {
                if (s.isBlank()) continue;
                statement.execute(s.trim() + ";");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public Connection connect() throws Exception {
        return dataSource.getConnection();
    }

    @Override
    public void close(Connection connection) throws Exception {
        connection.close();
    }
}
