package org.example.cafe.infrastructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.cafe.common.error.CafeException;
import org.example.cafe.domain.Reply;
import org.example.cafe.domain.Reply.ReplyBuilder;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.infrastructure.database.DbConnector;

public class ReplyJdbcRepository implements ReplyRepository {

    private static final String INSERT = "INSERT INTO REPLY (content, writer, question_id) VALUES (?, ?, ?)";
    private static final String SELECT_BY_QUESTION_ID = "SELECT * FROM REPLY WHERE question_id = ? and is_deleted = false";
    private static final String SELECT_BY_ID = "SELECT * FROM REPLY WHERE reply_id = ? and is_deleted = false";
    private static final String DELETE = "DELETE FROM REPLY";
    private static final String UPDATE_BY_ID = "UPDATE REPLY SET content = ?, writer = ?, question_id = ?, is_deleted = ? WHERE reply_id = ?";

    private final DbConnector dbConnector;

    public ReplyJdbcRepository(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public Long save(Reply reply) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, reply.getContent());
            preparedStatement.setString(2, reply.getWriter());
            preparedStatement.setLong(3, reply.getQuestionId());

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to insert reply");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new CafeException("Cannot get generate key");
            }

            return generatedKeys.getLong(1);
        } catch (SQLException e) {
            throw new CafeException("Failed to insert reply " + reply.getReplyId(), e);
        }
    }

    @Override
    public Reply findById(Long id) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new ReplyBuilder()
                            .replyId(resultSet.getLong("reply_id"))
                            .writer(resultSet.getString("writer"))
                            .content(resultSet.getString("content"))
                            .isDeleted(resultSet.getBoolean("is_deleted"))
                            .questionId(resultSet.getLong("question_id"))
                            .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime()).build();
                }
                return null;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find reply " + id, e);
        }
    }

    @Override
    public List<Reply> findByQuestionId(Long questionId) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_QUESTION_ID)) {
            preparedStatement.setLong(1, questionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Reply> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(new ReplyBuilder()
                            .replyId(resultSet.getLong("reply_id"))
                            .writer(resultSet.getString("writer"))
                            .content(resultSet.getString("content"))
                            .isDeleted(resultSet.getBoolean("is_deleted"))
                            .questionId(resultSet.getLong("question_id"))
                            .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime()).build());
                }

                return result;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find replies", e);
        }
    }

    @Override
    public void update(Reply reply) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_ID)) {
            preparedStatement.setString(1, reply.getContent());
            preparedStatement.setString(2, reply.getWriter());
            preparedStatement.setLong(3, reply.getQuestionId());
            preparedStatement.setBoolean(4, reply.getIsDeleted());
            preparedStatement.setLong(5, reply.getReplyId());

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to update reply " + reply.getReplyId());
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to update reply " + reply.getReplyId(), e);
        }
    }

    public void deleteAll() {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CafeException("Failed to delete replies", e);
        }
    }
}
