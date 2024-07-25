package lass9436.user.model;

import lass9436.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryDBImpl implements UserRepository {

	@Override
	public User save(User user) {
		String query;
		if (user.getUserSeq() > 0) {
			query = "UPDATE users SET userId = ?, password = ?, name = ?, email = ? WHERE userSeq = ?";
		} else {
			query = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
		}

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			statement.setString(1, user.getUserId());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getName());
			statement.setString(4, user.getEmail());

			if (user.getUserSeq() > 0) {
				statement.setLong(5, user.getUserSeq());
			}

			statement.executeUpdate();

			if (user.getUserSeq() == 0) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
					user.setUserSeq(generatedKeys.getLong(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User findByUserId(String userId) {
		String query = "SELECT * FROM users WHERE userId = ?";
		User user = null;

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, userId);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = mapRowToUser(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public List<User> findAll() {
		String query = "SELECT * FROM users";
		List<User> users = new ArrayList<>();

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				users.add(mapRowToUser(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	@Override
	public void deleteByUserId(String userId) {
		String query = "DELETE FROM users WHERE userId = ?";

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, userId);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findByUserSeq(long userSeq) {
		String query = "SELECT * FROM users WHERE userSeq = ?";
		User user = null;

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setLong(1, userSeq);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				user = mapRowToUser(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	private User mapRowToUser(ResultSet resultSet) throws SQLException {
		User user = new User();
		user.setUserSeq(resultSet.getLong("userSeq"));
		user.setUserId(resultSet.getString("userId"));
		user.setPassword(resultSet.getString("password"));
		user.setName(resultSet.getString("name"));
		user.setEmail(resultSet.getString("email"));
		return user;
	}
}
