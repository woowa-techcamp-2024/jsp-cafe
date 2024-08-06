package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.page.Page;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.Question;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLQuestionRepository implements QuestionRepository {
    private final DataSource ds;

    public MySQLQuestionRepository(DatabaseManager dm) {
        this.ds = dm.getDataSource();
    }

    @Override
    public Long save(Question question) {
        try (var conn = ds.getConnection(); var pstmt = conn.prepareStatement("INSERT INTO question (title, content, writer_id, created_at) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);){
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getContent());
            pstmt.setLong(3, question.getWriterId());
            pstmt.setObject(4, LocalDateTime.now());
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
        try (var conn = ds.getConnection(); var pstmt = conn.prepareStatement("SELECT q.id AS id, q.title AS title, q.content AS content, u.user_id AS writer, q.writer_id AS writer_id, q.created_at AS created_at FROM question q, user u WHERE q.is_deleted = FALSE AND q.writer_id = u.id");){
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("created_at").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return questions;
    }

    @Override
    public Question findById(Long id) {
        try (var conn = ds.getConnection(); var pstmt = conn.prepareStatement("SELECT q.id AS id, q.title AS title, q.content AS content, u.user_id AS writer, q.writer_id AS writer_id, q.created_at AS create_at FROM question q, user u WHERE q.id = ? AND q.is_deleted = FALSE AND q.writer_id = u.id");){
            pstmt.setLong(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Question(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("create_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return null;
    }

    @Override
    public Page<Question> findAllWithPage(PageRequest pageRequest) {
        try (var conn = ds.getConnection()){
            conn.setAutoCommit(false);

            var pstmt = conn.prepareStatement("SELECT q.id AS id, q.title AS title, q.content AS content, u.user_id AS writer, q.writer_id AS writer_id, q.created_at AS created_at FROM question q LEFT JOIN user u ON q.writer_id = u.id WHERE q.is_deleted = FALSE ORDER BY q.id DESC LIMIT ? OFFSET ?");
            pstmt.setInt(1, pageRequest.getSize());
            pstmt.setInt(2, pageRequest.getOffset());
            var rs = pstmt.executeQuery();

            var pstmt2 = conn.prepareStatement("SELECT COUNT(*) FROM question WHERE is_deleted = FALSE");
            var rs2 = pstmt2.executeQuery();
            rs2.next();

            int total = rs2.getInt(1);

            conn.commit();

            List<Question> questions = new ArrayList<>();
            while (rs.next()) {
                questions.add(new Question(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getString("writer"), rs.getLong("writer_id"), rs.getTimestamp("created_at").toLocalDateTime()));
            }

            return new Page<>(questions, pageRequest.getPage(), (int) (Math.floor(total / pageRequest.getSize())) + 1);
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (var conn = ds.getConnection(); var pstmt = conn.prepareStatement("DELETE FROM question");){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void update(Question target) {
        try (var conn = ds.getConnection(); var pstmt = conn.prepareStatement("UPDATE question SET title = ?, content = ? WHERE id = ?");){
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
        try (var conn = ds.getConnection();){
            conn.setAutoCommit(false);
            var pstmt = conn.prepareStatement("UPDATE question SET is_deleted = TRUE WHERE id = ?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            var pstmt2 = conn.prepareStatement("UPDATE reply SET is_deleted = TRUE WHERE question_id = ?");
            pstmt2.setLong(1, id);
            pstmt2.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
