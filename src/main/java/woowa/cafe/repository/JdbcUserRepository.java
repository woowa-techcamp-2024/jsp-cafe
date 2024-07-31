package woowa.cafe.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.cafe.config.JdbcConfig;
import woowa.cafe.domain.User;
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
public class JdbcUserRepository implements UserRepository {

    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(JdbcUserRepository.class);

    public JdbcUserRepository(JdbcConfig config) {
        this.dataSource = config.getDataSource();
        createTable();
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                       "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                       "userId VARCHAR(255), " +
                       "password VARCHAR(255), " +
                       "`name` VARCHAR(255), " +
                       "email VARCHAR(255))";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
            logger.info("create table user success");
        } catch (SQLException e) {
            logger.error("create table error", e);
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                Field idField = User.class.getDeclaredField("id");
                idField.setAccessible(true);
                User entity = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                idField.set(entity, id);
                users.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET userId = ?, password = ?, name = ?, email = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(String id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Field idField = User.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    User entity = new User(
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    idField.set(entity, id);
                    return entity;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findByUserIdAndPassword(String userId, String password) {
        String query = "SELECT * FROM users WHERE userId = ? AND password = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    Field idField = User.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    User entity = new User(
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    idField.set(entity, id);
                    return entity;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}