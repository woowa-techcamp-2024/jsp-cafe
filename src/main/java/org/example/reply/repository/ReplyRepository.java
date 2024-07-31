package org.example.reply.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.reply.model.ReplyStatus;
import org.example.reply.model.dao.Reply;
import org.example.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReplyRepository {

    private static final Logger logger = LoggerFactory.getLogger(ReplyRepository.class);
    private DataUtil dataUtil;

    @Autowired
    public ReplyRepository(DataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public Reply save(Reply reply) throws SQLException {
        logger.info("Saving reply: {}", reply);
        String sql = "insert into replies (post_id, writer, contents, status, created_at) values (?, ?, ?, ?, ?)";

        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reply.getPostId());
            ps.setString(2, reply.getWriter());
            ps.setString(3, reply.getContents());
            ps.setString(4, reply.getReplyStatus().name());
            ps.setObject(5, reply.getCreatedAt());
            ps.executeUpdate();
            return reply;
        } catch (SQLException e) {
            logger.error("Error saving post", e);
            throw new SQLException(e);
        }
    }

    public Reply findById(Long id) throws SQLException {
        String sql = "SELECT * FROM replies WHERE id = ? AND status = ?";

        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, PostStatus.AVAILABLE.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long postId = rs.getLong("post_id");
                    String writer = rs.getString("writer");
                    String contents = rs.getString("contents");
                    String status = rs.getString("status");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    Reply reply = Reply.createWithAll(id, postId, writer, contents, ReplyStatus.valueOf(status),
                            createdAt);
                    return reply;
                }
            }
        }
        throw new SQLException("User not found");
    }

    public List<Reply> findAll() throws SQLException {
        String sql = "SELECT * FROM posts WHERE status = ?";
        List<Reply> replies = new ArrayList<>();
        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, PostStatus.AVAILABLE.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    Long postId = rs.getLong("post_id");
                    String writer = rs.getString("writer");
                    String contents = rs.getString("contents");
                    String status = rs.getString("status");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    Reply reply = Reply.createWithAll(id, postId, writer, contents, ReplyStatus.valueOf(status),
                            createdAt);
                    replies.add(reply);
                }
            }
            return replies;
        } catch (SQLException e) {
            throw new SerialException(e.getMessage());
        }
    }

    public Reply update(Reply reply) throws SQLException {
        logger.info("Updating reply: {}", reply);
        String sql = "UPDATE posts SET contents = ? WHERE id = ?";

        try (Connection conn = dataUtil.getConnection();
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

        try (Connection conn = dataUtil.getConnection();
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
