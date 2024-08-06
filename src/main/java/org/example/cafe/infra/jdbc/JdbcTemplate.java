package org.example.cafe.infra.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.example.cafe.common.exception.CafeException;
import org.example.cafe.infra.jdbc.exception.JdbcTemplateException;

public class JdbcTemplate {

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static void setArgPreparedStatement(Object[] args, PreparedStatement preparedStatement)
            throws SQLException {
        int argLen = args.length;
        for (int i = 0; i < argLen; i++) {
            if (args[i] instanceof String sargs) {
                preparedStatement.setString(i + 1, sargs);
            } else if (args[i] instanceof Integer iargs) {
                preparedStatement.setInt(i + 1, iargs);
            } else if (args[i] instanceof Long largs) {
                preparedStatement.setLong(i + 1, largs);
            } else if (args[i] instanceof Boolean bargs) {
                preparedStatement.setBoolean(i + 1, bargs);
            } else if (args[i] instanceof LocalDateTime largs) {
                preparedStatement.setTimestamp(i + 1, Timestamp.valueOf(largs));
            } else {
                throw new JdbcTemplateException("Unsupported type " + args[i].getClass());
            }
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (args != null) {
                setArgPreparedStatement(args, preparedStatement);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(rowMapper.mapRow(resultSet, resultSet.getRow()));
                }

                return result;
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException("Failed to query " + sql);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> results = query(sql, rowMapper, args);
        if (results.size() > 1) {
            throw new JdbcTemplateException("Failed to query: result size is " + results.size());
        }
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public void update(String sql, KeyHolder keyHolder, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            setArgPreparedStatement(args, preparedStatement);

            preparedStatement.executeUpdate();

            if (keyHolder != null) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    throw new CafeException("Cannot get generate key");
                }

                keyHolder.setKey(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new JdbcTemplateException("Failed to update " + sql);
        }
    }
}


