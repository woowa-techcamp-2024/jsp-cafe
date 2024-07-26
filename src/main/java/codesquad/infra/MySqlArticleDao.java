package codesquad.infra;

import codesquad.domain.article.Article;
import codesquad.domain.article.ArticleDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlArticleDao implements ArticleDao {
    @Override
    public Long save(Article article) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "insert into articles(title,writer,content) values(?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getWriter());
            pstmt.setString(3, article.getContent());
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                article = new Article(id, article);
                return id;
            }
            throw new SQLException("Failed to insert article");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "select * from articles where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                return Optional.of(new Article(id, title, writer, content));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Article> findAll() {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "select * from articles";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            List<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                articles.add(new Article(id, title, writer, content));
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
