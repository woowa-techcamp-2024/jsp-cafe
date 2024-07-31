package woowa.camp.jspcafe.repository.reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class DBReplyRepository implements ReplyRepository {

    private final DatabaseConnector connector;

    public DBReplyRepository(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public Reply save(Reply reply) {
        String sql = "INSERT INTO replies (user_id, article_id, content, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setLong(1, reply.getUserId());
            pstmt.setLong(2, reply.getArticleId());
            pstmt.setString(3, reply.getContent());
            pstmt.setTimestamp(4, Timestamp.valueOf(reply.getCreatedAt()));
            pstmt.setTimestamp(5, Timestamp.valueOf(reply.getUpdatedAt()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("댓글 저장을 실패했습니다. 영향을 받은 행이 없습니다.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reply.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("게시글 저장을 실패했습니다. id를 획득하지 못했습니다.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reply;
    }

    @Override
    public Optional<Reply> findById(Long id) {
        String sql = "SELECT * FROM replies WHERE id = ? AND deleted_at IS NULL";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToReply(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        List<Reply> replies = new ArrayList<>();
        String sql = "SELECT * FROM replies WHERE article_id = ? AND deleted_at IS NULL";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    replies.add(mapRowToReply(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return replies;
    }

    @Override
    public void softDeleteByUserId(Long userId, LocalDateTime deletedTime) {
        String sql = "UPDATE replies SET deleted_at = ? WHERE user_id = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(deletedTime));
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reply mapRowToReply(ResultSet rs) throws SQLException {
        Reply reply = new Reply(
                rs.getLong("user_id"),
                rs.getLong("article_id"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                null
        );
        reply.setId(rs.getLong("id"));
        return reply;
    }
}
