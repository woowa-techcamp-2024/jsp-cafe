package org.example.post.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.dao.Post;
import org.example.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);
    private DataUtil dataUtil;

    @Autowired
    public PostRepository(DataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public Post save(Post post) throws SQLException {
        logger.info("Saving post: {}", post);
        String sql = "insert into posts (writer, title, contents) values (?, ?, ?)";

        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, post.getWriter());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContents());
            ps.executeUpdate();
            return post;
        } catch (SQLException e) {
            logger.error("Error saving post", e);
            throw new SQLException(e);
        }
    }

    public Post findById(Long id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";

        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String writer = rs.getString("writer");
                    String title = rs.getString("title");
                    String contents = rs.getString("contents");

                    Post post = Post.create(writer, title, contents);
                    return post;
                }
            }
        }
        throw new SQLException("User not found");
    }

    public List<Post> findAll() throws SQLException {
        String sql = "SELECT * FROM posts";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String writer = rs.getString("writer");
                String title = rs.getString("title");
                String contents = rs.getString("contents");
                Post post = Post.createWithId(id, writer, title, contents);
                posts.add(post);
            }
            return posts;
        } catch (SQLException e) {
            throw new SerialException("Database Error");
        }
    }
}
