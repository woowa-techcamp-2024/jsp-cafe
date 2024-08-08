package cafe.questions.repository;

import cafe.database.ConnectionPool;
import cafe.questions.Reply;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReplyRepository implements ReplyRepository {
    private final ConnectionPool connectionPool;

    public JdbcReplyRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Reply save(Reply reply) {
        if (reply.getId() == null) {
            return insert(reply);
        } else {
            return update(reply);
        }
    }

    private Reply insert(Reply reply) {
        String sql = "INSERT INTO replies (article_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, reply.getArticleId());
            statement.setLong(2, reply.getUserId());
            statement.setString(3, reply.getContent());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to save reply, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return reply.withId(generatedKeys.getLong(1));
                } else {
                    throw new RuntimeException("Failed to save reply, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Reply update(Reply reply) {
        String sql = "UPDATE replies SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND deleted_at IS NULL";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reply.getContent());
            statement.setLong(2, reply.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to update reply, no rows affected.");
            }
            return reply;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reply findById(Long id) {
        String sql = """
                    SELECT r.*, u.username
                    FROM replies r
                    JOIN users u ON r.user_id = u.id
                    WHERE r.id = ? AND r.deleted_at IS NULL
                """;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Reply(
                            resultSet.getLong("id"),
                            resultSet.getLong("article_id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("content")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Reply> findByArticleId(Long articleId) {
        String sql = """
                    SELECT r.*, u.username
                    FROM replies r
                    JOIN users u ON r.user_id = u.id
                    WHERE r.article_id = ? AND r.deleted_at IS NULL
                """;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, articleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Reply> replies = new ArrayList<>();
                while (resultSet.next()) {
                    replies.add(new Reply(
                            resultSet.getLong("id"),
                            resultSet.getLong("article_id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("content")
                    ));
                }
                return replies;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteById(Long id) {
        String sql = "UPDATE replies SET deleted_at = CURRENT_TIMESTAMP WHERE id = ? AND deleted_at IS NULL";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reply> findByArticleId(Long articleId, Long cursor, int limit) {
        String sql = """
                    SELECT r.*, u.username
                    FROM replies r
                    JOIN users u ON r.user_id = u.id
                    WHERE r.article_id = ? AND r.deleted_at IS NULL
                    AND r.id > ?
                    LIMIT ?
                """;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, articleId);
            statement.setLong(2, cursor);
            statement.setInt(3, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Reply> replies = new ArrayList<>();
                while (resultSet.next()) {
                    replies.add(new Reply(
                            resultSet.getLong("id"),
                            resultSet.getLong("article_id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("content")
                    ));
                }
                return replies;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reply> findByArticleIdAndUserId(Long articleId, Long userId) {
        String sql = """
                SELECT r.*, u.username
                FROM replies r
                JOIN users u ON r.user_id = u.id
                WHERE r.article_id = ? AND r.user_id = ? AND r.deleted_at IS NULL
            """;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, articleId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Reply> replies = new ArrayList<>();
                while (resultSet.next()) {
                    replies.add(new Reply(
                            resultSet.getLong("id"),
                            resultSet.getLong("article_id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("content")
                    ));
                }
                return replies;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
