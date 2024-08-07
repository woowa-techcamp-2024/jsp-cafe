package woowa.cafe.repository;

import woowa.cafe.config.JdbcConfig;
import woowa.cafe.domain.Reply;
import woowa.cafe.dto.Offset;
import woowa.frame.core.annotation.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcReplyRepository implements ReplyRepository {

    private final DataSource dataSource;

    public JdbcReplyRepository(JdbcConfig config) {
        this.dataSource = config.getDataSource();
        createTable();
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS replies (" +
                       "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                       "content VARCHAR(255), " +
                       "createdDate DATETIME, " +
                       "status VARCHAR(20), " +
                       "userId VARCHAR(255), " +
                       "authorName VARCHAR(255), " +
                       "questionId VARCHAR(20))";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Reply reply) {
        String query = "INSERT INTO replies (content, createdDate, status, userId, authorName, questionId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, reply.getContent());
            ps.setObject(2, reply.getCreatedDate());
            ps.setString(3, reply.getStatus());
            ps.setString(4, reply.getUserId());
            ps.setString(5, reply.getAuthorName());
            ps.setString(6, reply.getQuestionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reply> findAll() {
        List<Reply> replies = new ArrayList<>();
        String query = "SELECT * FROM replies WHERE status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                Field idField = Reply.class.getDeclaredField("id");
                idField.setAccessible(true);
                Reply entity = new Reply(
                        rs.getString("content"),
                        rs.getString("status"),
                        rs.getString("userId"),
                        rs.getString("authorName"),
                        rs.getString("questionId")
                );
                idField.set(entity, id);
                replies.add(entity);
            }
            return replies;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reply> findAllByQuestionId(String questionId, Offset offset) {

        String query = "SELECT * FROM replies WHERE questionId = ? and status != 'DELETED' ORDER BY createdDate DESC LIMIT ?, ?;";
        List<Reply> replies = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, questionId);
            ps.setLong(2, offset.offset());
            ps.setLong(3, offset.size());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Field idField = Reply.class.getDeclaredField("id");
                idField.setAccessible(true);
                Reply entity = new Reply(
                        rs.getString("content"),
                        rs.getString("status"),
                        rs.getString("userId"),
                        rs.getString("authorName"),
                        rs.getString("questionId")
                );
                idField.set(entity, id);
                replies.add(entity);
            }
            return replies;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reply findById(String id) {
        String query = "SELECT * FROM replies WHERE id = ? and status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long replyId = rs.getLong("id");
                    Field idField = Reply.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    Reply reply = new Reply(
                            rs.getString("content"),
                            rs.getString("status"),
                            rs.getString("userId"),
                            rs.getString("authorName"),
                            rs.getString("questionId")
                    );
                    idField.set(reply, replyId);
                    return reply;
                }
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        String query = "UPDATE replies SET status = 'DELETED' WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByQuestionId(String questionId) {
        String query = "UPDATE replies SET status = 'DELETED' WHERE questionId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, questionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long count(String questionId) {
        String query = "SELECT COUNT(*) FROM replies WHERE questionId = ? and status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("count query error");
    }

    @Override
    public boolean existsByQuestionIdAndNotUserId(String questionId, String userId) {
        String query = "SELECT count(*) AS counts FROM replies WHERE questionId = ? and userId != ? and status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, questionId);
            ps.setString(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("counts");
                    if (count > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
