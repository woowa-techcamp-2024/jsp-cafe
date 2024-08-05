package codesquad.jspcafe.domain.reply.repository;

import codesquad.jspcafe.common.database.JDBCConnectionManager;
import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.user.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReplyJdbcRepository implements ReplyRepository {

    private static final Logger log = LoggerFactory.getLogger(ReplyJdbcRepository.class);
    private static final String SCHEMA_DDL = """
        CREATE TABLE IF NOT EXISTS replies
        (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            article BIGINT NOT NULL,
            user BIGINT NOT NULL,
            contents TEXT NOT NULL,
            created_at TIMESTAMP NOT NULL,
            deleted_at TIMESTAMP
        );""";
    private final JDBCConnectionManager connectionManager;

    public ReplyJdbcRepository(JDBCConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        init();
    }

    @Override
    public Reply save(Reply reply) {
        String insertQuery = "INSERT INTO replies (article, user, contents, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, reply.getArticle());
            preparedStatement.setLong(2, reply.getWriter().getId());
            preparedStatement.setString(3, reply.getContents());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(reply.getCreatedAt()));
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                Long replyId = generatedKey.getLong(1);
                reply.setId(replyId);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return reply;
    }

    @Override
    public Reply update(Reply reply) {
        String updateQuery = "UPDATE replies SET contents = ? WHERE id = ? AND deleted_at IS NULL";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, reply.getContents());
            preparedStatement.setLong(2, reply.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return reply;
    }

    @Override
    public Optional<Reply> findById(Long id) {
        String selectQuery = "SELECT r.id, r.article, u.id, u.user_id, u.password, u.username, u.email, r.contents, r.created_at FROM replies r, users u WHERE r.user = u.id AND r.id = ? AND r.deleted_at IS NULL";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Reply reply = getReply(resultSet);
                return Optional.of(reply);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        List<Reply> results = new ArrayList<>();
        String selectQuery = "SELECT r.id, r.article, u.id, u.user_id, u.password, u.username, u.email, r.contents, r.created_at FROM replies r, users u WHERE r.user = u.id AND r.article = ? AND r.deleted_at IS NULL ORDER BY r.id LIMIT 6";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(getReply(resultSet));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(results);
    }

    @Override
    public List<Reply> findByArticleId(Long articleId, Long replyId) {
        List<Reply> results = new ArrayList<>();
        String selectQuery = "SELECT r.id, r.article, u.id, u.user_id, u.password, u.username, u.email, r.contents, r.created_at FROM replies r, users u WHERE r.user = u.id AND r.article = ? AND r.id > ? AND r.deleted_at IS NULL ORDER BY r.id LIMIT 6";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, articleId);
            preparedStatement.setLong(2, replyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(getReply(resultSet));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(results);
    }

    @Override
    public Long delete(Reply reply) {
        String deleteQuery = "UPDATE replies SET deleted_at = NOW() WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, reply.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return reply.getId();
    }

    private void init() {
        try (Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(SCHEMA_DDL);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private Reply getReply(ResultSet resultSet) throws SQLException {
        return new Reply(
            resultSet.getLong("r.id"),
            resultSet.getLong("r.article"),
            new User(
                resultSet.getLong("u.id"),
                resultSet.getString("u.user_id"),
                resultSet.getString("u.password"),
                resultSet.getString("u.username"),
                resultSet.getString("u.email")
            ),
            resultSet.getString("r.contents"),
            resultSet.getTimestamp("r.created_at").toLocalDateTime()
        );
    }
}
