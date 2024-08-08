package com.codesquad.cafe.db.dao;

import com.codesquad.cafe.db.rowmapper.ResultSetExtractor;
import com.codesquad.cafe.db.rowmapper.RowMapper;
import com.codesquad.cafe.exception.DBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTemplate {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveAndGetGeneratedKey(String sql, Consumer<PreparedStatement> statementConsumer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statementConsumer.accept(pstmt);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating record failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        } catch (SQLException exception) {
            log.warn("fail to execute and generate key : {}", sql);
            throw new DBException("Failed to execute" + sql);
        }
    }

    public <T> T queryForObject(String sql, Consumer<PreparedStatement> statementConsumer, RowMapper<T> rowMapper) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            statementConsumer.accept(pstmt);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return rowMapper.mapRow(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            log.warn("fail to query for object : {} {}", sql, e.getMessage());
            throw new DBException("Failed to query object : " + sql + e.getMessage());
        }
    }

    public <T> T queryForObject(String sql, Consumer<PreparedStatement> statementConsumer,
                                ResultSetExtractor<T> resultSetExtractor) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            statementConsumer.accept(pstmt);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSetExtractor.extractData(resultSet);
            }
        } catch (SQLException e) {
            log.warn("fail to query for object : {} {}", sql, e.getMessage());
            throw new DBException("Failed to query object : " + sql + e.getMessage());
        }
    }

    public <T> List<T> queryForList(String sql, Consumer<PreparedStatement> statementConsumer, RowMapper<T> rowMapper) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            statementConsumer.accept(pstmt);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(rowMapper.mapRow(resultSet));
                }
                return results;
            }
        } catch (SQLException e) {
            log.warn("fail to query for list : {}", sql);
            throw new DBException("Failed to query list : " + e.getMessage());
        }
    }

    public int executeUpdate(String sql, Consumer<PreparedStatement> statementConsumer) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            statementConsumer.accept(pstmt);
            int affectedRows = 0;
            try {
                affectedRows = pstmt.executeUpdate();
            } catch (SQLException exception) {
                connection.rollback();
                throw new SQLException();
            } finally {
                connection.setAutoCommit(true);
            }
            return affectedRows;
        } catch (SQLException e) {
            log.warn("faile to execute : {}", sql);
            throw new DBException("Failed to execute : " + sql);
        }
    }

    public void executeUpdateInTransaction(Map<String, Consumer<PreparedStatement>> statements) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            for (Entry<String, Consumer<PreparedStatement>> entry : statements.entrySet()) {
                try (PreparedStatement statement = connection.prepareStatement(entry.getKey())) {
                    entry.getValue().accept(statement);
                    statement.executeUpdate();
                }
            }
            log.debug("transaction committed");
            connection.commit();
        } catch (SQLException e) {
            log.warn("rollbacked while transction");
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    log.warn("fail to rollback");
                }
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                log.warn("fail to close connection");
            }
        }
    }
}
