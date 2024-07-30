package lass9436.question.model;

import lass9436.config.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepositoryDBImpl implements QuestionRepository {

	@Override
	public Question save(Question question) {
		String query;
		if (question.getQuestionSeq() > 0) {
			query = "UPDATE questions SET userSeq = ?, writer = ?, title = ?, contents = ? WHERE questionSeq = ?";
		} else {
			query = "INSERT INTO questions (userSeq, writer, title, contents) VALUES (?, ?, ?, ?)";
		}

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setLong(1, question.getUserSeq());
			statement.setString(2, question.getWriter());
			statement.setString(3, question.getTitle());
			statement.setString(4, question.getContents());

			if (question.getQuestionSeq() > 0) {
				statement.setLong(5, question.getQuestionSeq());
			}

			statement.executeUpdate();

			if (question.getQuestionSeq() == 0) {
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
					question.setQuestionSeq(generatedKeys.getLong(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return question;
	}

	@Override
	public Question findByQuestionSeq(long questionSeq) {
		String query = "SELECT * FROM questions WHERE questionSeq = ?";
		Question question = null;

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setLong(1, questionSeq);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				question = mapRowToQuestion(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return question;
	}

	@Override
	public List<Question> findAll() {
		String query = "SELECT * FROM questions";
		List<Question> questions = new ArrayList<>();

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				questions.add(mapRowToQuestion(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return questions;
	}

	@Override
	public void deleteByQuestionSeq(long questionSeq) {
		String query = "DELETE FROM questions WHERE questionSeq = ?";

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setLong(1, questionSeq);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Question findByTitle(String title) {
		String query = "SELECT * FROM questions WHERE title = ?";
		Question question = null;

		try (Connection connection = Database.getConnection();
			 PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, title);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				question = mapRowToQuestion(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return question;
	}

	private Question mapRowToQuestion(ResultSet resultSet) throws SQLException {
		Question question = new Question();
		question.setQuestionSeq(resultSet.getLong("questionSeq"));
		question.setUserSeq(resultSet.getLong("userSeq"));
		question.setWriter(resultSet.getString("writer"));
		question.setTitle(resultSet.getString("title"));
		question.setContents(resultSet.getString("contents"));
		return question;
	}
}
