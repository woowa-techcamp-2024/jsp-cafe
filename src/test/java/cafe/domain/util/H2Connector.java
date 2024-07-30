package cafe.domain.util;

import cafe.util.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import org.h2.jdbcx.JdbcConnectionPool;

public class H2Connector implements DatabaseConnector {
    private static final String DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:file:./db/cafe";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static JdbcConnectionPool dataSource;

    static {
        try {
            Class.forName(DRIVER);
            FileUtil.createFile(System.getProperty("user.home") + File.separator +
                    "db" + File.separator + "cafe.mv.db");

            dataSource = JdbcConnectionPool.create(DB_URL, DB_USER, DB_PASSWORD);
            dataSource.setMaxConnections(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public H2Connector() {
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
                sql.append(line).append("\n");
            }
            statement.execute(sql.toString());
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
