package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLReplyRepository implements ReplyRepository {
    private final Connection conn;

    public MySQLReplyRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Long save(Reply reply) {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reply (content, question_id, writer, writer_id) VALUES (?, ?, ?, ?) ", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, reply.getContent());
            pstmt.setLong(2, reply.getQuestionId());
            pstmt.setString(3, reply.getWriter());
            pstmt.setLong(4, reply.getWriterId());
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
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reply WHERE id = ?")) {
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

    }

    @Override
    public List<Reply> findByQuestionId(Long questionId) {
        List<Reply> replies = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reply WHERE question_id = ?")) {
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
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reply WHERE id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
