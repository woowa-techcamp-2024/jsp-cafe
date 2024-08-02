package woopaca.jspcafe.repository;

import woopaca.jspcafe.database.JdbcTemplate;
import woopaca.jspcafe.model.Reply;

import java.util.List;
import java.util.Optional;

public class ReplyMySQLRepository implements ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReplyMySQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Reply reply) {
        if (reply.getId() != null) {
            update(reply);
            return;
        }

        String sql = "INSERT INTO reply (content, written_at, writer_id, post_id, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, reply.getContent(), reply.getWrittenAt(), reply.getWriterId(), reply.getPostId(), reply.getStatus().name());
    }

    private void update(Reply reply) {
        String sql = "UPDATE reply SET content = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, reply.getContent(), reply.getStatus().name(), reply.getId());
    }

    @Override
    public List<Reply> findByPostId(Long postId) {
        String sql = "SELECT * FROM reply WHERE post_id = ?";
        return jdbcTemplate.queryForList(sql, Reply.class, postId);
    }

    @Override
    public Optional<Reply> findById(Long id) {
        String sql = "SELECT * FROM reply WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Reply.class, id));
    }
}
