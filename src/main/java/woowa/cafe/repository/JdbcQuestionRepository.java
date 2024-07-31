package woowa.cafe.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.cafe.config.JdbcConfig;
import woowa.cafe.domain.Question;
import woowa.frame.core.annotation.Component;
import woowa.frame.core.annotation.Primary;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                       "userId VARCHAR(255)" +
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
        String query = "INSERT INTO questions (authorName, title, content, userId) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, question.getAuthorName());
            ps.setString(2, question.getTitle());
            ps.setString(3, question.getContent());
            ps.setString(4, question.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                Field idField = Question.class.getDeclaredField("id");
                idField.setAccessible(true);
                Question entity = new Question(
                        rs.getString("authorName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("userId")
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
        String query = "SELECT * FROM questions WHERE id = ?";
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
                            rs.getString("userId")
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
        String query = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}