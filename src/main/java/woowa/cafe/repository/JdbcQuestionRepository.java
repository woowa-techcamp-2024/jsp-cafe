package woowa.cafe.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.cafe.config.JdbcConfig;
import woowa.cafe.domain.Question;
import woowa.cafe.dto.Pageable;
import woowa.frame.core.annotation.Component;
import woowa.frame.core.annotation.Primary;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class JdbcQuestionRepository implements QuestionRepository {

    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(JdbcQuestionRepository.class);

    public JdbcQuestionRepository(JdbcConfig config) {
        this.dataSource = config.getDataSource();
        createTable();
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS questions (" +
                       "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                       "authorName VARCHAR(255), " +
                       "title VARCHAR(255), " +
                       "content VARCHAR(255)," +
                       "userId VARCHAR(255)," +
                       "status VARCHAR(20)," +
                       "createdAt DATETIME" +
                       ")";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
            logger.info("create table question success");
        } catch (SQLException e) {
            logger.error("create table error", e);
            e.printStackTrace();
        }
    }

    @Override
    public void save(Question question) {
        String query = "INSERT INTO questions (authorName, title, content, userId, status, createdAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, question.getAuthorName());
            ps.setString(2, question.getTitle());
            ps.setString(3, question.getContent());
            ps.setString(4, question.getUserId());
            ps.setString(5, question.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(question.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> findAll(Pageable pageable) {
        if (pageable == null) {
            pageable = new Pageable(1, 15, "createdAt");
        }

        List<Question> questions = new ArrayList<>();
        String subquery = "SELECT id FROM questions WHERE status != 'DELETED' ORDER BY " + pageable.sort() + " DESC LIMIT ?, ?";
        String query = "SELECT * FROM (" + subquery + ") as q1 JOIN questions as q2 on q1.id = q2.id;";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setLong(1, pageable.getOffset());
            ps.setLong(2, pageable.getSize());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                Field idField = Question.class.getDeclaredField("id");
                idField.setAccessible(true);
                Question entity = new Question(
                        rs.getString("authorName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("userId"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt").toLocalDateTime()
                );
                idField.set(entity, id);
                questions.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return questions;
    }

    @Override
    public Question findById(String id) {
        String query = "SELECT * FROM questions WHERE id = ? and status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Field idField = Question.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    Question entity = new Question(
                            rs.getString("authorName"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("userId"),
                            rs.getString("status"),
                            rs.getTimestamp("createdAt").toLocalDateTime()
                    );
                    idField.set(entity, id);
                    return entity;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Question question) {

        String query = "UPDATE questions SET " +
                       "authorName = ?, " +
                       "title = ?, " +
                       "content = ?, " +
                       "userId = ? " +
                       "WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, question.getAuthorName());
            ps.setString(2, question.getTitle());
            ps.setString(3, question.getContent());
            ps.setString(4, question.getUserId());
            ps.setString(5, question.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(String id) {
        String query = "UPDATE questions SET status = 'DELETED' WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM questions WHERE status != 'DELETED'";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
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
}