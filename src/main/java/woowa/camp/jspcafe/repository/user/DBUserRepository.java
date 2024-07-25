package woowa.camp.jspcafe.repository.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;

public class DBUserRepository implements UserRepository {

    private final DatabaseConnector connector;

    public DBUserRepository(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public Long save(User user) {
        String sql = "INSERT INTO users (email, nickname, password, register_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getPassword());
            pstmt.setObject(4, user.getRegisterAt()); // LocalDate should be handled accordingly

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    user.setId(generatedId);
                    return generatedId;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }

        return 0L;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }

        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public User update(User user, UserUpdateRequest userUpdateRequest) {
        String sql = "UPDATE users SET nickname = ?, password = ? WHERE id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, userUpdateRequest.nickname());
            pstmt.setString(2, userUpdateRequest.password());
            pstmt.setLong(3, user.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("회원정보 갱신을 실패했습니다. 영향을 받은 행이 없습니다.");
            }

            return new User(user.getEmail(), userUpdateRequest.nickname(), userUpdateRequest.password(),
                    user.getRegisterAt());
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User(
                resultSet.getString("email"),
                resultSet.getString("nickname"),
                resultSet.getString("password"),
                resultSet.getObject("register_at", LocalDate.class)
        );
        user.setId(resultSet.getLong("id"));
        return user;
    }

}
