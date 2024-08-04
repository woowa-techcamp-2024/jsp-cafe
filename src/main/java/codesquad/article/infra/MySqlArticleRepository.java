package codesquad.article.infra;

import codesquad.article.domain.Article;
import codesquad.article.domain.vo.Status;
import codesquad.article.repository.ArticleRepository;
import codesquad.common.db.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlArticleRepository implements ArticleRepository {
    private ConnectionManager connectionManager;

    public MySqlArticleRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Long save(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "insert into articles(title,writer,content,status) values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getWriter());
            preparedStatement.setString(3, article.getContent());
            preparedStatement.setString(4, article.getStatus().name());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                article = new Article(id, article);
                return id;
            }
            throw new SQLException("Failed to insert article");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from articles where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                return Optional.of(new Article(id, title, writer, content, Status.of(status)));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<Article> findByIdForUpdate(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from articles where id = ? for update";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                return Optional.of(new Article(id, title, writer, content, Status.of(status)));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<Article> findByIdForShare(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from articles where id = ? for share";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                return Optional.of(new Article(id, title, writer, content, Status.of(status)));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<Article> findAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from articles";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String writer = resultSet.getString("writer");
                String content = resultSet.getString("content");
                String status = resultSet.getString("status");
                articles.add(new Article(id, title, writer, content, Status.of(status)));
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void update(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "update articles set content = ?, status = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, article.getContent());
            preparedStatement.setString(2, article.getStatus().name());
            preparedStatement.setLong(3, article.getId());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Failed to update article");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement);
        }
    }
}
