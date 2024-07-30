package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.entity.User;
import com.example.exception.BaseException;

public class UserMysqlDatabase implements UserDatabase {

	private static final String URL = "jdbc:mysql://localhost:3306/codesquad";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	@Override
	public void insert(User user) {
		String sql = "insert into user (id, password, name, email) values (?, ?, ?, ?)";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, user.id());
			pstmt.setString(2, user.password());
			pstmt.setString(3, user.name());
			pstmt.setString(4, user.email());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public Optional<User> findById(String id) {
		String sql = "select * from user where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			User user = null;
			while (rs.next()) {
				String password = rs.getString("password");
				String name = rs.getString("name");
				String email = rs.getString("email");
				user = new User(id, password, name, email);
			}
			return Optional.ofNullable(user);
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<User> findAll() {
		String sql = "select * from user";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			ResultSet rs = pstmt.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String email = rs.getString("email");
				users.add(new User(id, password, name, email));
			}
			return users;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void update(String id, User user) {
		String sql = "update user set name = ?, email = ? where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, user.name());
			pstmt.setString(2, user.email());
			pstmt.setString(3, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}
}
