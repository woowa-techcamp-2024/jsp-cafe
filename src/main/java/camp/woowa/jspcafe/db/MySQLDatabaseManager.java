package camp.woowa.jspcafe.db;

import camp.woowa.jspcafe.utils.ConfigLoader;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class MySQLDatabaseManager implements DatabaseManager{
    private MysqlDataSource dataSource;

    public MySQLDatabaseManager() {
        Properties props = ConfigLoader.loadConfig();
        dataSource = new MysqlDataSource();
        dataSource.setUrl(props.getProperty("DB_URL"));
        dataSource.setUser(props.getProperty("DB_USER"));
        dataSource.setPassword(props.getProperty("DB_PASSWORD"));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
