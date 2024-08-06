package cafe.domain.db;

import cafe.domain.entity.Comment;
import cafe.domain.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommentDatabase implements Database<String, Comment> {
    private final DatabaseConnector databaseConnector;

    public CommentDatabase(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public DatabaseConnector getConnector() {
        return databaseConnector;
    }

    public List<Comment> selectCommentsByArticleId(String articleId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = databaseConnector.connect()) {
            String query = "SELECT * FROM `comments` WHERE `articleId` = ? ORDER BY `created` ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, articleId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Object deleted = resultSet.getObject("deleted");
                if (deleted != null && (boolean) deleted) continue;

                String commentId = resultSet.getString("commentId");
                String userId = resultSet.getString("userId");
                String contents = resultSet.getString("contents");
                String created = resultSet.getString("created");
                comments.add(Comment.of(commentId, userId, articleId, contents, created));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public void deleteByArticleId(String articleId) {
        try (Connection connection = databaseConnector.connect()) {
            String query = "UPDATE comments SET deleted = true WHERE articleId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, articleId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
