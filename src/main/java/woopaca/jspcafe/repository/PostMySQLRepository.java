package woopaca.jspcafe.repository;

import woopaca.jspcafe.database.JdbcTemplate;
import woopaca.jspcafe.model.Post;

import java.util.List;
import java.util.Optional;

public class PostMySQLRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostMySQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO post (title, content, written_at, writer) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getWrittenAt(), post.getWriter());
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM post";
        return jdbcTemplate.queryForList(sql, Post.class);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        String sql = "SELECT * FROM post WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Post.class, postId));
    }
}
