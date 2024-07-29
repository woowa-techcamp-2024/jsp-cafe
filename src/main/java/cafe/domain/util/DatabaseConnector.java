package cafe.domain.util;

import java.sql.Connection;

public interface DatabaseConnector {
    void init();
    Connection connect() throws Exception;
    void close(Connection connection) throws Exception;
}
