package woowa.cafe.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import woowa.cafe.config.JdbcConfig;

import javax.sql.DataSource;

public class TestJdbcConfig extends JdbcConfig {

    private final DataSource dataSource;

    public TestJdbcConfig(String jdbcUrl, String user, String password) {
        if (System.getenv("DB_URL") != null) {
            jdbcUrl = System.getenv("DB_URL");
        }

        if (System.getenv("DB_USER") != null) {
            user = System.getenv("DB_USER");
        }

        if (System.getenv("DB_PASSWORD") != null) {
            password = System.getenv("DB_PASSWORD");
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
