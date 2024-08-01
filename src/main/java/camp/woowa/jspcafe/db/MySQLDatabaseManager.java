package camp.woowa.jspcafe.db;

import camp.woowa.jspcafe.utils.ConfigLoader;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.MysqlDataSourceFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class MySQLDatabaseManager implements DatabaseManager {
    private DataSource dataSource;

    public MySQLDatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = ConfigLoader.loadConfig();
            HikariConfig hikariCPConfig = new HikariConfig();
            hikariCPConfig.setJdbcUrl(props.getProperty("DB_URL"));
            hikariCPConfig.setUsername(props.getProperty("DB_USER"));
            hikariCPConfig.setPassword(props.getProperty("DB_PASSWORD"));

            dataSource = new HikariDataSource(hikariCPConfig);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
