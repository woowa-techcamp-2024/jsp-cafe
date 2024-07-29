package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserRepository implements UserRepository{
    private final Connection conn;

    public MySQLUserRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Long save(String userId, String password, String name, String email) {
        try (var pstmt = conn.prepareStatement("INSERT INTO user (userId, password, name, email) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);){
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, email);
            pstmt.executeUpdate();

            try (var gk = pstmt.getGeneratedKeys()) {
                if (!gk.next()) {
                    throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return gk.getLong(1);
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public User findById(Long userId) {
        try (var pstmt = conn.prepareStatement("SELECT * FROM user WHERE id = ?");){
            pstmt.setLong(1, userId);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (var pstmt = conn.prepareStatement("SELECT * FROM user");){
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return users;
    }

    @Override
    public Long update(Long id, String updatedName, String updatedEmail) {
        try (var pstmt = conn.prepareStatement("UPDATE user SET name = ?, email = ? WHERE id = ?");){
            pstmt.setString(1, updatedName);
            pstmt.setString(2, updatedEmail);
            pstmt.setLong(3, id);

            pstmt.executeUpdate();

            return id;
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAll() {
        try (var pstmt = conn.prepareStatement("DELETE FROM user");){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isExistedByUserId(String userId) {
        try (var pstmt = conn.prepareStatement("SELECT * FROM user WHERE userId = ?");){
            pstmt.setString(1, userId);
            var rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public User findByUserId(String w) {
        try (var pstmt = conn.prepareStatement("SELECT * FROM user WHERE userId = ?");){
            pstmt.setString(1, w);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
