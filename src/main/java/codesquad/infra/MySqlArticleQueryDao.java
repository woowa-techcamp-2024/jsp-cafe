package codesquad.infra;

import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.servlet.dto.ArticleResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlArticleQueryDao implements ArticleQueryDao {
    @Override
    public Optional<ArticleResponse> findById(Long id) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "SELECT a.id AS articleId, a.title, a.content, u.id AS writerId, u.user_id AS writer " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.writer = u.user_id " +
                    "WHERE a.id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Long articleId = resultSet.getLong("articleId");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Long writerId = resultSet.getLong("writerId");
                String writer = resultSet.getString("writer");
                return Optional.of(new ArticleResponse(articleId, title, content, writerId, writer));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ArticleResponse> findAll() {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "SELECT a.id AS articleId, a.title, a.content, u.id AS writerId, u.user_id AS writer " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.writer = u.user_id";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            List<ArticleResponse> articles = new ArrayList<>();
            while (resultSet.next()) {
                Long articleId = resultSet.getLong("articleId");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Long writerId = resultSet.getLong("writerId");
                if (writerId == null) {
                    writerId = 0L;
                }
                String writer = resultSet.getString("writer");
                if (writer == null) {
                    writer = "알수없는 사용자";
                }
                articles.add(new ArticleResponse(articleId, title, content, writerId, writer));
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
