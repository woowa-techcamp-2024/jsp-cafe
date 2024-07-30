package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.entity.Article;
import com.example.exception.BaseException;

public class ArticleMysqlDatabase implements ArticleDatabase {

	private static final String URL = "jdbc:mysql://localhost:3306/codesquad";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	@Override
	public void insert(Article article) {
		String sql = "insert into article (userId, title, contents, createdAt) values (?, ?, ?, ?)";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, article.getUserId());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContents());
			pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedAt()));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public Optional<Article> findById(Long id) {
		String sql = "select * from article where id = ?";
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
				article = new Article(id, userId, title, contents, createdAt);
			}
			return Optional.ofNullable(article);
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}

	@Override
	public List<Article> findAll() {
		String sql = "select * from article";
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
				articles.add(new Article(id, userId, title, contents, createdAt));
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
		String sql = "delete from article where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql)
		) {
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw BaseException.serverException();
		}
	}
}
