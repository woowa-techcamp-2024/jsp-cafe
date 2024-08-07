package cafe.domain.db;

import cafe.domain.util.DatabaseConnector;
import cafe.domain.entity.Article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArticleDatabase implements Database<String, Article> {
    DatabaseConnector databaseConnector;

    public ArticleDatabase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public DatabaseConnector getConnector() {
        return databaseConnector;
    }

    public Map<String, Article> selectArticleByPage(int page) {
        try (Connection connection = databaseConnector.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM articles WHERE deleted = false LIMIT 15 OFFSET ?");
            preparedStatement.setInt(1, (page - 1) * 15);

            ResultSet resultSet = preparedStatement.executeQuery();
            Map<String, Article> articles = new LinkedHashMap<>();
            while (resultSet.next()) {
                String id = resultSet.getString("articleId");
                String writer = resultSet.getString("writer");
                String title = resultSet.getString("title");
                String contents = resultSet.getString("contents");
                Article article = Article.of(id, writer, title, contents);
                articles.put(id, article);
            }
            return articles;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int selectArticleCount() {
        try (Connection connection = databaseConnector.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM articles WHERE deleted = false");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
