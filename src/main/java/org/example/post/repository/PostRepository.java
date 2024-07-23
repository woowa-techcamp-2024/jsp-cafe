package org.example.post.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.post.model.dao.Post;
import org.example.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);

    public Post save(Post post) throws SQLException {
        logger.info("Saving post: {}", post);
        String sql = "insert into posts (writer, title, contents) values (?, ?, ?)";

        try (Connection conn = DataUtil.getConnection();
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
}
