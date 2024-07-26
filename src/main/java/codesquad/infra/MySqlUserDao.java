package codesquad.infra;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.exception.DuplicateIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlUserDao implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(MySqlUserDao.class);

    @Override
    public Long save(User user) throws DuplicateIdException {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "insert into users(userId,password,name,email) values(?,?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                user = new User(id, user);
                return id;
            }
            throw new SQLException("Failed to insert article");
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateIdException("중복된 아이디 입니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "select * from users where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return Optional.of(new User(id, userId, password, name, email));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "select * from users where user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return Optional.of(new User(id, userId, password, name, email));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "select * from users";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String userId = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                users.add(new User(id, userId, password, name, email));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = MySqlConnectionManager.getConnection()) {
            String sql = "update users set name = ?, email = ? where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setLong(3, user.getId());
            int updatedRows = pstmt.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Failed to update user");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
