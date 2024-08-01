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

import com.example.entity.Article;
import com.example.exception.BaseException;

public class ArticleMysqlDatabase implements ArticleDatabase {

	@Override
	public Long insert(Article article) {
		String sql = "insert into article (userId, title, contents, createdAt, userName) values (?, ?, ?, ?, ?)";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		) {
			pstmt.setString(1, article.getUserId());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContents());
			pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedAt()));
			pstmt.setString(5, article.getUserName());
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
	public Optional<Article> findById(Long id) {
		String sql = "select * from article where id = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, String.valueOf(id));
			ResultSet rs = pstmt.executeQuery();
			Article article = null;
			while (rs.next()) {
				String userId = rs.getString("userId");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				String userName = rs.getString("userName");
				article = new Article(id, userId, title, contents, createdAt, false, userName);
			}
			return Optional.ofNullable(article);
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<Article> findAll() {
		String sql = "select * from article where deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			ResultSet rs = pstmt.executeQuery();
			List<Article> articles = new ArrayList<>();
			while (rs.next()) {
				Long id = rs.getLong("id");
				String userId = rs.getString("userId");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				LocalDateTime createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
				String userName = rs.getString("userName");
				articles.add(new Article(id, userId, title, contents, createdAt, false, userName));
			}
			return articles;
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void update(Long id, Article article) {
		String sql = "update article set title = ?, contents = ?, createdAt = ? where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContents());
			pstmt.setTimestamp(3, Timestamp.valueOf(article.getCreatedAt()));
			pstmt.setLong(4, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void delete(Long id) {
		String sql = "update article set deleted = ? where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setBoolean(1, true);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public void updateUserName(String userId, String updateName) {
		String sql = "update article set userName = ? where userId = ? and deleted=false";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setString(1, updateName);
			pstmt.setString(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}
}
