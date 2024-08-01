package codesqaud;

import codesqaud.app.db.JdbcTemplate;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestDataSource {
    private static final TestDataSource INSTANCE = new TestDataSource();
    private static final String URL = "jdbc:h2:mem:testdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private final JdbcConnectionPool jdbcConnectionPool;


    private TestDataSource() {
        try {
            initServer();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(URL);
        jdbcDataSource.setUser(USER);
        jdbcDataSource.setPassword(PASSWORD);
        this.jdbcConnectionPool = JdbcConnectionPool.create(jdbcDataSource);
    }

    private void initServer() throws SQLException {
        Server.createTcpServer("-tcp", "-tcpPort", "8092").start();
    }

    public static DataSource getDataSource() {
        return INSTANCE.jdbcConnectionPool;
    }

    public static void create() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(INSTANCE.jdbcConnectionPool);

        jdbcTemplate.execute("""
                 CREATE TABLE IF NOT EXISTS users (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     user_id VARCHAR(50) NOT NULL UNIQUE,
                     password VARCHAR(255) NOT NULL,
                     name VARCHAR(100) NOT NULL,
                     email VARCHAR(100) NOT NULL
                 );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS articles (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR (200) NOT NULL,
                    contents TEXT NOT NULL,
                    activate BOOLEAN NOT NULL,
                    author_id BIGINT REFERENCES users(id),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS replies (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    contents TEXT NOT NULL,
                    activate BOOLEAN NOT NULL,
                    article_id BIGINT REFERENCES articles(id),
                    author_id BIGINT REFERENCES users(id),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """);
    }

    public static void drop() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(INSTANCE.jdbcConnectionPool);
        jdbcTemplate.execute("DROP ALL OBJECTS");
    }
}
