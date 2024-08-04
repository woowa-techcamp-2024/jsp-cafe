package codesquad.common.db.connection;

import codesquad.common.exception.ExternalConnectionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerConnectionManager implements ConnectionManager {
    private final DataSource datasource;

    public ServerConnectionManager(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new ExternalConnectionException(e);
        }
    }
}
