package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.page.Page;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLReplyRepository implements ReplyRepository {
    private final DataSource ds;

    public MySQLReplyRepository(DatabaseManager dm) {
        this.ds = dm.getDataSource();
    }

    @Override
    public Long save(Reply reply) {
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reply (content, question_id, writer_id, created_at) VALUES (?, ?, ?, ?) ", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, reply.getContent());
            pstmt.setLong(2, reply.getQuestionId());
            pstmt.setLong(3, reply.getWriterId());
            pstmt.setObject(4, LocalDateTime.now());
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
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT r.id AS id, r.content AS content, r.question_id AS question_id, u.user_id AS writer, r.writer_id AS writer_id, r.created_at AS created_at FROM reply r, user u WHERE r.id = ? AND r.is_deleted = FALSE AND r.writer_id = u.id")) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Reply(rs.getLong("id"), rs.getString("content"), rs.getLong("question_id"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("created_at").toLocalDateTime());
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
        try (var conn = ds.getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT r.id AS id, r.content AS content, r.question_id AS question_id, u.user_id AS writer, r.writer_id AS writer_id, r.created_at AS created_at FROM reply r, user u WHERE r.question_id = ? AND r.is_deleted = FALSE AND r.writer_id = u.id")) {
            pstmt.setLong(1, questionId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                replies.add(new Reply(rs.getLong("id"), rs.getString("content"), rs.getLong("question_id"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("created_at").toLocalDateTime()));
            }
            return replies;
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Page<Reply> findByQuestionIdWithPage(Long questionId, PageRequest pageRequest) {
        List<Reply> replies = new ArrayList<>();
        try (var conn = ds.getConnection(); ) {
            conn.setAutoCommit(false);
            var pstmt1 = conn.prepareStatement("SELECT r.id AS id, r.content AS content, r.question_id AS question_id, u.user_id AS writer, r.writer_id AS writer_id, r.created_at AS created_at FROM reply r, user u WHERE r.question_id = ? AND r.is_deleted = FALSE AND r.writer_id = u.id ORDER BY r.id DESC LIMIT ? OFFSET ?");
            pstmt1.setLong(1, questionId);
            pstmt1.setInt(2, pageRequest.getSize());
            pstmt1.setInt(3, pageRequest.getOffset());
            ResultSet rs = pstmt1.executeQuery();

            var pstmt2 = conn.prepareStatement("SELECT COUNT(*) FROM reply WHERE question_id = ? AND is_deleted = FALSE");
            pstmt2.setLong(1, questionId);
            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();
            int total = rs2.getInt(1);
            conn.commit();

            while(rs.next()) {
                replies.add(new Reply(rs.getLong("id"), rs.getString("content"), rs.getLong("question_id"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("created_at").toLocalDateTime()));
            }
            return new Page<>(replies, pageRequest.getPage() , (int) (Math.floor(total / pageRequest.getSize())) + 1);
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
