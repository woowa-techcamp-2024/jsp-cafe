package com.example.db;

import static com.example.utils.Constant.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.entity.Reply;
import com.example.exception.BaseException;

public class ReplyMysqlDatabase implements ReplyDatabase {

	@Override
	public Long insert(Reply reply) {
		String sql = "insert into reply (contents, createdAt, deleted, articleId, userId, userName) values (?, ?, ?, ?, ?, ?)";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		) {
			pstmt.setString(1, reply.getContents());
			pstmt.setTimestamp(2, Timestamp.valueOf(reply.getCreatedAt()));
			pstmt.setBoolean(3, reply.isDeleted());
			pstmt.setLong(4, reply.getArticleId());
			pstmt.setString(5, reply.getUserId());
			pstmt.setString(6, reply.getUserName());
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
			return 0L;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public Optional<Reply> findById(Long id) {
		String sql = "select * from reply where id = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			Reply reply = null;
			while (rs.next()) {
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				Long articleId = rs.getLong("articleId");
				String userId = rs.getString("userId");
				String userName = rs.getString("userName");
				reply = new Reply(id, contents, createdAt, deleted, articleId, userId, userName);
			}
			return Optional.ofNullable(reply);
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<Reply> findAll() {
		String sql = "select * from reply where deleted = false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			ResultSet rs = pstmt.executeQuery();
			List<Reply> replies = new ArrayList<>();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				Long articleId = rs.getLong("articleId");
				String userId = rs.getString("userId");
				String userName = rs.getString("userName");
				replies.add(new Reply(id, contents, createdAt, deleted, articleId, userId, userName));
			}
			return replies;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void update(Long id, Reply reply) {
	}

	@Override
	public void delete(Long id) {
		String sql = "update reply set deleted = ? where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setBoolean(1, true);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	public long countByArticleId(Long articleId) {
		String sql = "select count(*) as count from reply where articleId = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setLong(1, articleId);
			ResultSet rs = pstmt.executeQuery();
			long count = 0;
			if (rs.next()) {
				count = rs.getLong("count");
			}
			return count;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<Reply> findByArticleId(Long articleId) {
		String sql = "select * from reply where articleId = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setLong(1, articleId);
			ResultSet rs = pstmt.executeQuery();
			List<Reply> replies = new ArrayList<>();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				String userId = rs.getString("userId");
				String userName = rs.getString("userName");
				replies.add(new Reply(id, contents, createdAt, deleted, articleId, userId, userName));
			}
			return replies;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<Reply> findByArticleIdWithPagination(Long articleId, Long lastReplyId, LocalDateTime lastCreatedAt) {
		String sql = """
				select * from reply 
			        where articleId = ? and deleted=false
					and createdAt >= ?
					and (createdAt > ? or id > ?)
				order by createdAt
				limit 6
			""";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setLong(1, articleId);
			pstmt.setTimestamp(2, Timestamp.valueOf(lastCreatedAt));
			pstmt.setTimestamp(3, Timestamp.valueOf(lastCreatedAt));
			pstmt.setLong(4, lastReplyId);
			ResultSet rs = pstmt.executeQuery();
			List<Reply> replies = new ArrayList<>();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				boolean deleted = rs.getBoolean("deleted");
				String userId = rs.getString("userId");
				String userName = rs.getString("userName");
				replies.add(new Reply(id, contents, createdAt, deleted, articleId, userId, userName));
			}
			return replies;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void deleteByArticleId(Long articleId) {
		String sql = "update reply set deleted = ? where articleId = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setBoolean(1, true);
			pstmt.setLong(2, articleId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void updateUserName(String userId, String updateName) {
		String sql = "update reply set userName = ? where userId = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, userId);
			pstmt.setString(2, updateName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}
}
