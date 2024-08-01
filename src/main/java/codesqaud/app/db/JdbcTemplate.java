package codesqaud.app.db;

import codesqaud.app.db.exception.DbConstraintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T queryForObject(String sql, RowMapper<T> resultSetMapper, Object... values) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= values.length; i++) {
                preparedStatement.setObject(i, values[i - 1]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSetMapper.map(resultSet);
                }
                return null;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DbConstraintException(e.getMessage());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }

    public Object queryForObject(String sql, Object... values) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= values.length; i++) {
                preparedStatement.setObject(i, values[i - 1]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(1);
            } else {
                return null;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DbConstraintException(e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> resultSetMapper, Object... values) {
        List<T> results = new ArrayList<>();
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= values.length; i++) {
                preparedStatement.setObject(i, values[i - 1]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(resultSetMapper.map(resultSet));
            }
            logger.debug("executeQuery: {}", sql);
            return results;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DbConstraintException(e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }

    public int update(String sql, Object... values) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= values.length; i++) {
                preparedStatement.setObject(i, values[i - 1]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DbConstraintException(e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }

    public void execute(String sql) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (Statement statement = connection.createStatement()) {
            logger.debug("executeQuery: {}", sql);
            statement.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection);
        }
    }
}