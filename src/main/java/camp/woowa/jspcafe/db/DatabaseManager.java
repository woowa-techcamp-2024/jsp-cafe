package camp.woowa.jspcafe.db;

import javax.sql.DataSource;

public interface DatabaseManager {
    DataSource getDataSource();
}
