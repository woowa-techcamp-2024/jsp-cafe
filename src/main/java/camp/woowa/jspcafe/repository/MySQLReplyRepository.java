package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLReplyRepository implements ReplyRepository {
    private final DataSource ds;

    public MySQLReplyRepository(DatabaseManager dm) {
        this.ds = dm.getDataSource();
    }

    @Override
    public Long save(Reply reply) {
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reply (content, question_id, writer_id) VALUES (?, ?, ?) ", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, reply.getContent());
            pstmt.setLong(2, reply.getQuestionId());
            pstmt.setLong(3, reply.getWriterId());
            pstmt.executeUpdate();

            ResultSet gk = pstmt.getGeneratedKeys();

            if (gk.next()) {
                return gk.getLong(1);
            } else {
                throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get generated key");
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Reply findById(Long id) {
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT r.id AS id, r.content AS content, r.question_id AS question_id, u.user_id AS writer, r.writer_id AS writer_id FROM reply r, user u WHERE r.id = ? AND r.is_deleted = FALSE AND r.writer_id = u.id")) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Reply(rs.getLong("id"), rs.getString("content"), rs.getLong("question_id"), rs.getString("writer"), rs.getLong("writer_id"));
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND, "Reply not found");
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reply")) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<Reply> findByQuestionId(Long questionId) {
        List<Reply> replies = new ArrayList<>();
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT r.id AS id, r.content AS content, r.question_id AS question_id, u.user_id AS writer, r.writer_id AS writer_id FROM reply r, user u WHERE r.question_id = ? AND r.is_deleted = FALSE AND r.writer_id = u.id")) {
            pstmt.setLong(1, questionId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                replies.add(new Reply(rs.getLong("id"), rs.getString("content"), rs.getLong("question_id"), rs.getString("writer"), rs.getLong("writer_id")));
            }
            return replies;
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("UPDATE reply SET is_deleted = TRUE WHERE id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
