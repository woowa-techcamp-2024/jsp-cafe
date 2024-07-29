package cafe.domain.db;

import cafe.domain.DatabaseManager;
import cafe.domain.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase implements Database<String, User> {

    public User selectByUserId(String userid) {
        User user = null;

        String sql = "SELECT * FROM `users` WHERE `userid` = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userId = resultSet.getString("userid");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    user = User.of(userId, name, password, email);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}
