package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.entity.Article;

public class ArticleMysqlDatabase implements ArticleDatabase {

	private static final String URL = "jdbc:mysql://localhost:3306/codesquad";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	@Override
	public void insert(Article article) {
		String sql = "insert into article (writer, title, contents) values (?, ?, ?)";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContents());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
				String writer = rs.getString("writer");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				article = new Article(id, writer, title, contents);
			}
			return Optional.ofNullable(article);
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
				String writer = rs.getString("writer");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				articles.add(new Article(id, writer, title, contents));
			}
			return articles;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(Long id, Article article) {
		String sql = "update article set writer = ?, title = ?, contents = ? where id = ?";
		try (
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt = conn.prepareStatement(sql);
		) {
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getTitle());
			pstmt.setString(3, article.getContents());
			pstmt.setLong(4, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
