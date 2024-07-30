package cafe.domain.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.UUID;

public class SQLExecutor {
    private static SQLExecutor sqlExecutor;

    private SQLExecutor() { }

    public static SQLExecutor getInstance() {
        if (sqlExecutor == null) {
            sqlExecutor = new SQLExecutor();
        }
        return sqlExecutor;
    }

    public <V> void executeInsert(Connection connection, String insertSQL, Field[] fields, V data) throws SQLException, IllegalAccessException {
        PreparedStatement statement = connection.prepareStatement(insertSQL);
        for (int i = 0; i < fields.length; i++) {
            statement.setObject(i + 1, fields[i].get(data).toString());
        }
        statement.executeUpdate();
    }

    public <K> ResultSet executeSelectById(Connection connection, String selectSQL, K id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(selectSQL);
        statement.setObject(1, id);
        return statement.executeQuery();
    }

    public ResultSet executeSelectAll(Connection connection, String selectAllSQL) throws SQLException {
        return connection.prepareStatement(selectAllSQL).executeQuery();
    }

    public <V, K> void executeUpdate(Connection connection, String updateSQL, Field[] fields, V data, K id) throws SQLException, IllegalAccessException {
        PreparedStatement statement = connection.prepareStatement(updateSQL);
        for (int i = 0; i < fields.length; i++) {
            statement.setObject(i + 1, fields[i].get(data).toString());
        }
        statement.setObject(fields.length + 1, id);
        statement.executeUpdate();
    }
}
