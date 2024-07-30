package com.hyeonuk.jspcafe.article.dao;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.db.DBManager;
import com.hyeonuk.jspcafe.global.db.DBManagerIml;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlArticleDao implements ArticleDao{
    private final DBManager manager;

    public MysqlArticleDao(DBManager manager) {
        this.manager = manager;
    }

    @Override
    public Article save(Article article) {
        if(!article.validation()){
            throw new DataIntegrityViolationException("can't persist null or empty value");
        }
        try(Connection conn = manager.getConnection()) {
            if(article.getId() == null){//save작업
                String writer = article.getWriter();
                String title = article.getTitle();
                String contents = article.getContents();
                String query = "insert into article(writer,title,contents) values(?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, writer);
                pstmt.setString(2, title);
                pstmt.setString(3, contents);
                int num = pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()){
                    article.setId(rs.getLong(1));
                }
            }
            else{//update 작업
                Long id = article.getId();
                String writer = article.getWriter();
                String title = article.getTitle();
                String contents = article.getContents();
                Optional<Article> byId = findById(id);
                if(byId.isPresent()) {
                    String query = "update article set writer = ?, title = ?, contents = ? where id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, writer);
                    pstmt.setString(2, title);
                    pstmt.setString(3, contents);
                    pstmt.setLong(4, id);

                    pstmt.executeUpdate();
                }
                else{
                    String query = "insert into article(id,writer,title,contents) values(?,?,?,?)";
                    PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    pstmt.setLong(1,id);
                    pstmt.setString(2, writer);
                    pstmt.setString(3, title);
                    pstmt.setString(4, contents);

                    pstmt.executeUpdate();
                }
            }
            return article;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public List<Article> findAll() {
        try(Connection conn = manager.getConnection()){
            String sql = "select * from article";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<Article> articles = new ArrayList<>();
            while(rs.next()) {
                articles.add(mappingArticle(rs));
            }
            return articles;
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        try(Connection conn = manager.getConnection()){
            String sql = "select * from article where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            Article article = null;
            if(rs.next()) {
                article = mappingArticle(rs);
            }
            return Optional.ofNullable(article);
        } catch (SQLException e) {
            throw new DataIntegrityViolationException("error");
        }
    }

    private Article mappingArticle(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String writer = rs.getString("writer");
        String title = rs.getString("title");
        String contents = rs.getString("contents");
        return new Article(id,writer,title,contents);
    }
}
