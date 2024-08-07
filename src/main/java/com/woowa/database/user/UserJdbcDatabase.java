package com.woowa.database.user;

import com.woowa.database.DBConnectionUtils;
import com.woowa.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserJdbcDatabase implements UserDatabase {
    @Override
    public void save(User user) {
        String sql = "insert into user (user_id, email, password, nickname) values (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getNickname());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from user where email = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(User.create(
                        rs.getString("user_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from user";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = User.create(
                        rs.getString("user_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        String sql = "select * from user where user_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(User.create(
                        rs.getString("user_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, rs);
        }
    }

    @Override
    public void update(User user) {
        String sql = "update user set user_id = ?, email = ?, password = ?, nickname = ? where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getNickname());
            pstmt.setString(5, user.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("SQL 예외", e);
        } finally {
            DBConnectionUtils.closeConnection(con, pstmt, null);
        }
    }
}
