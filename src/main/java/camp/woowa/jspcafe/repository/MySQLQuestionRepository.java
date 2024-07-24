package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.models.Question;

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
            return pstmt.getGeneratedKeys().getLong(1);
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
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
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
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
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    public void deleteAll() {
        try (var pstmt = conn.prepareStatement("DELETE FROM question");){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
