package repository.article;

import domain.Article;
import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCArticleRepository implements ArticleRepository {

    private static final Logger log = LoggerFactory.getLogger(JDBCArticleRepository.class);

    @Override
    public void saveArticle(Article article) {
        String sql = "INSERT INTO Articles (writer_id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, article.getWriter().getId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.executeUpdate();
            pstmt.close();
            DatabaseUtils.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT Users.id as writer_id," +
                "userId, password, name, email," +
                "Articles.id as article_id," +
                "title, content, created " +
                "FROM Users, Articles where Users.id = Articles.writer_id";

        List<Article> articles = new ArrayList<>();
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                articles.add(new Article(
                        rs.getLong("article_id"),
                        new User(
                                rs.getLong("writer_id"),
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email")),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created")
                ));
            }
            pstmt.close();
            DatabaseUtils.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public Optional<Article> findById(Long id) {
        String sql = "SELECT Users.id as writer_id," +
                "userId, password, name, email," +
                "Articles.id as article_id," +
                "title, content, created " +
                "FROM Users, Articles where Users.id = Articles.writer_id and Articles.id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Article(
                        rs.getLong("article_id"),
                        new User(
                                rs.getLong("writer_id"),
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email")),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created")
                ));
            }
            pstmt.close();
            DatabaseUtils.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void updateArticle(Article article) {
        String sql = "UPDATE Articles SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setLong(3, article.getId());
            pstmt.executeUpdate();
            DatabaseUtils.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteArticle(Long id) {
        String sql = "DELETE FROM Articles WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            DatabaseUtils.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
