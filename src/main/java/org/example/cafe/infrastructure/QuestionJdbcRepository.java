package org.example.cafe.infrastructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.cafe.common.error.CafeException;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.infrastructure.database.DbConnector;

public class QuestionJdbcRepository implements QuestionRepository {

    private static final String INSERT = "INSERT INTO QUESTION (title, content, writer) VALUES (?, ?, ?)";
    private static final String SELECT = "SELECT * FROM QUESTION";
    private static final String SELECT_BY_ID = "SELECT * FROM QUESTION WHERE question_id = ?";
    private static final String DELETE = "DELETE FROM QUESTION";
    private static final String DELETE_BY_ID = "DELETE FROM QUESTION WHERE question_id = ?";

    private final DbConnector dbConnector;

    public QuestionJdbcRepository(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Long save(Question question) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, question.getTitle());
            preparedStatement.setString(2, question.getContent());
            preparedStatement.setString(3, question.getWriter());

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to insert question " + question.getQuestionId());
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new CafeException("Cannot get generate key");
            }

            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            throw new CafeException("Failed to insert question " + question.getQuestionId(), e);
        }
    }

    @Override
    public Question findById(Long id) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Question(resultSet.getLong("question_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writer"),
                            resultSet.getTimestamp("created_at").toLocalDateTime());
                }
                return null;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find question " + id, e);
        }
    }

    public List<Question> findAll() {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Question> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(new Question(resultSet.getLong("question_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writer"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()));
                }

                return result;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find questions", e);
        }
    }

    public void delete(Long id) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to delete question " + id);
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to delete question " + id, e);
        }
    }

    public void deleteAll() {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CafeException("Failed to delete Questions", e);
        }
    }
}
