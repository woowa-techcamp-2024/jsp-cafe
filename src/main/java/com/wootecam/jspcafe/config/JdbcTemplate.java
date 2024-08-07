package com.wootecam.jspcafe.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplate {

    private final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSourceManager dataSourceManager;

    public JdbcTemplate(final DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    public void update(final String query, final PreparedStatementSetter psSetter) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            psSetter.setValues(ps);

            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void update(final String query, final PreparedStatementSetter psSetter, final KeyHolder keyHolder) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            psSetter.setValues(ps);
            ps.executeUpdate();

            holdGeneratedKey(keyHolder, ps);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void holdGeneratedKey(final KeyHolder keyHolder, final PreparedStatement ps)
            throws SQLException {
        try (ResultSet keyResultSet = ps.getGeneratedKeys()) {
            if (keyResultSet.next()) {
                keyHolder.setId(keyResultSet.getLong(1));
            }
        }
    }

    public <T> T selectOne(final String query, final PreparedStatementSetter setter,
                           final ResultSetMapper<T> mapper) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            setter.setValues(ps);

            return getValue(mapper, ps);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T selectOne(final String query, final ResultSetMapper<T> mapper) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            return getValue(mapper, ps);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private <T> T getValue(final ResultSetMapper<T> mapper, final PreparedStatement ps) throws SQLException {
        T value = null;

        try (ResultSet resultSet = ps.executeQuery()) {
            if (resultSet.next()) {
                value = mapper.map(resultSet);
            }
        }

        return value;
    }

    public <T> List<T> selectAll(final String query, final ResultSetMapper<T> mapper) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {

            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(mapper.map(resultSet));
            }

            return results;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> selectAll(final String query,
                                 final PreparedStatementSetter setter, final ResultSetMapper<T> mapper) {
        try (Connection conn = dataSourceManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            setter.setValues(ps);

            return getValues(mapper, ps);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> getValues(final ResultSetMapper<T> mapper, final PreparedStatement ps)
            throws SQLException {
        try (ResultSet resultSet = ps.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(mapper.map(resultSet));
            }
            return results;
        }
    }

    public void execute(final String query) {
        try (Connection conn = dataSourceManager.getConnection();
             Statement statement = conn.createStatement()) {

            statement.execute(query);

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
