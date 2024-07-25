package com.woowa.hyeonsik.server.database;

import com.woowa.hyeonsik.server.database.property.DatabaseProperty;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.function.Function;

public class DatabaseConnector {
private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
    private final DataSource dataSource;

    public DatabaseConnector(DatabaseProperty databaseProperty) {
        final DataSourceManager dataSourceManager = new DataSourceManager(databaseProperty);
        dataSource = dataSourceManager.getDataSource();
    }

    public void execute(String query) {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public void execute(String query, List<String> values) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public <T> T executeQuery(String query, Function<ResultSet, T> mapper) {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
        ) {
            return mapper.apply(resultSet);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public <T> T executeQuery(String query, List<String> values, Function<ResultSet, T> mapper) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setString(i + 1, values.get(i));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.apply(resultSet);
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }
}
