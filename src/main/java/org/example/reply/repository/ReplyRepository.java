package org.example.reply.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.reply.model.ReplyStatus;
import org.example.reply.model.dao.Reply;
import org.example.reply.model.dto.ReplyDto;
import org.example.util.DatabaseConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReplyRepository {

    private static final Logger logger = LoggerFactory.getLogger(ReplyRepository.class);
    private DatabaseConnectionPool connectionPool;

    @Autowired
    public ReplyRepository(DatabaseConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Reply save(Reply reply) throws SQLException {
        logger.info("Saving reply: {}", reply);
        String sql = "insert into replies (post_id, user_id, contents, status, created_at) values (?, ?, ?, ?, ?)";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            long id;
            ps.setLong(1, reply.getPostId());
            ps.setString(2, reply.getUserId());
            ps.setString(3, reply.getContents());
            ps.setString(4, reply.getReplyStatus().name());
            ps.setObject(5, reply.getCreatedAt());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating reply failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating reply failed, no ID obtained.");
                }
            }

            return Reply.createWithAll(id, reply.getPostId(), reply.getUserId(), reply.getContents(),
                    reply.getReplyStatus(), reply.getCreatedAt());
        } catch (SQLException e) {
            logger.error("Error saving reply", e);
            throw new SQLException(e);
        }
    }

    public ReplyDto findById(Long id) throws SQLException {
        String sql = "SELECT r.id, r.post_id, r.user_id, u.name as writer, r.contents, r.status, r.created_at " +
                "FROM replies r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "WHERE r.id = ? AND r.status = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, PostStatus.AVAILABLE.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long postId = rs.getLong("post_id");
                    String userId = rs.getString("user_id");
                    String writer = rs.getString("writer");
                    String contents = rs.getString("contents");
                    String status = rs.getString("status");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    return new ReplyDto.Builder()
                            .id(id)
                            .postId(postId)
                            .userId(userId)
                            .writer(writer)
                            .contents(contents)
                            .replyStatus(ReplyStatus.valueOf(status))
                            .createdAt(createdAt)
                            .build();
                }
            }
        }
        throw new SQLException("Reply not found");
    }

    public List<ReplyDto> findAll(Long postId) throws SQLException {
        String sql = "SELECT r.id, r.user_id, u.name as writer, r.contents, r.status, r.created_at " +
                "FROM replies r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "WHERE r.post_id = ? AND r.status = ?";
        List<ReplyDto> replies = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, postId);
            ps.setString(2, PostStatus.AVAILABLE.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String userId = rs.getString("user_id");
                    String writer = rs.getString("writer");
                    String contents = rs.getString("contents");
                    String status = rs.getString("status");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    ReplyDto reply = new ReplyDto.Builder()
                            .id(id)
                            .postId(postId)
                            .userId(userId)
                            .writer(writer)
                            .contents(contents)
                            .replyStatus(ReplyStatus.valueOf(status))
                            .createdAt(createdAt)
                            .build();
                    replies.add(reply);
                }
            }
            return replies;
        } catch (SQLException e) {
            throw new SerialException(e.getMessage());
        }
    }

    public ReplyDto update(ReplyDto reply) throws SQLException {
        String sql = "UPDATE replies SET contents = ? WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reply.getContents());
            ps.setLong(2, reply.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating reply failed, no rows affected.");
            }

            return reply;
        } catch (SQLException e) {
            logger.error("Error updating reply", e);
            throw new SQLException(e);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "UPDATE replies SET status = ? WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ReplyStatus.DELETED.name());
            ps.setLong(2, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating post failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.error("Error updating post", e);
            throw new SQLException(e);
        }
    }
}
