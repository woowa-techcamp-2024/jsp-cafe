package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Question;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLQuestionRepository implements QuestionRepository {
    private final Connection conn;

    public MySQLQuestionRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Long save(String title, String content, String writer, Long writerId) {
        try (var pstmt = conn.prepareStatement("INSERT INTO question (title, content, writer, writerId) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);){
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, writer);
            pstmt.setLong(4, writerId);
            pstmt.executeUpdate();

            try (var gk = pstmt.getGeneratedKeys()) {
                if (!gk.next()) {
                    throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get generated key.");
                }
                return gk.getLong(1);
            }

        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        try (var pstmt = conn.prepareStatement("SELECT * FROM question");){
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getString("writer"), rs.getLong("writerId")));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return questions;
    }

    @Override
    public Question findById(Long id) {
        try (var pstmt = conn.prepareStatement("SELECT * FROM question WHERE id = ?");){
            pstmt.setLong(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Question(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getString("writer"), rs.getLong("writerId"));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAll() {
        try (var pstmt = conn.prepareStatement("DELETE FROM question");){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void update(Question target) {
        try (var pstmt = conn.prepareStatement("UPDATE question SET title = ?, content = ? WHERE id = ?");){
            pstmt.setString(1, target.getTitle());
            pstmt.setString(2, target.getContent());
            pstmt.setLong(3, target.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()) ;
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var pstmt = conn.prepareStatement("DELETE FROM question WHERE id = ?");){
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
