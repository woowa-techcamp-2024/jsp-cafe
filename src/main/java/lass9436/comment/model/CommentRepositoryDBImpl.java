package lass9436.comment.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lass9436.config.Database;

public class CommentRepositoryDBImpl implements CommentRepository {

	@Override
	public Comment save(Comment comment) {
		String sqlInsert = "INSERT INTO comments (userSeq, questionSeq, contents) VALUES (?, ?, ?)";
		String sqlUpdate = "UPDATE comments SET userSeq = ?, questionSeq = ?, contents = ? WHERE commentSeq = ?";

		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = comment.getCommentSeq() == 0 ?
				 conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sqlUpdate)) {

			if (comment.getCommentSeq() == 0) {
				ps.setLong(1, comment.getUserSeq());
				ps.setLong(2, comment.getQuestionSeq());
				ps.setString(3, comment.getContents());
				ps.executeUpdate();

				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						comment.setCommentSeq(generatedKeys.getLong(1));
					}
				}
			} else {
				ps.setLong(1, comment.getUserSeq());
				ps.setLong(2, comment.getQuestionSeq());
				ps.setString(3, comment.getContents());
				ps.setLong(4, comment.getCommentSeq());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comment;
	}

	@Override
	public Comment findByCommentSeq(long commentSeq) {
		String sql = "SELECT * FROM comments WHERE commentSeq = ?";
		Comment comment = null;

		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, commentSeq);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					comment = mapRow(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comment;
	}

	@Override
	public List<Comment> findAll() {
		String sql = "SELECT * FROM comments";
		List<Comment> comments = new ArrayList<>();

		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				comments.add(mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comments;
	}

	@Override
	public void deleteByCommentSeq(long commentSeq) {
		String sql = "DELETE FROM comments WHERE commentSeq = ?";

		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, commentSeq);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> findByQuestionSeq(long questionSeq) {
		String sql = "SELECT * FROM comments WHERE questionSeq = ?";
		List<Comment> comments = new ArrayList<>();

		try (Connection conn = Database.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, questionSeq);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					comments.add(mapRow(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comments;
	}

	private Comment mapRow(ResultSet rs) throws SQLException {
		Comment comment = new Comment();
		comment.setCommentSeq(rs.getLong("commentSeq"));
		comment.setUserSeq(rs.getLong("userSeq"));
		comment.setQuestionSeq(rs.getLong("questionSeq"));
		comment.setContents(rs.getString("contents"));
		return comment;
	}
}
