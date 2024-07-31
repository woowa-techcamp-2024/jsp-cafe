package woopaca.jspcafe.repository;

import woopaca.jspcafe.database.JdbcTemplate;
import woopaca.jspcafe.model.Reply;

public class ReplyMySQLRepository implements ReplyRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReplyMySQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Reply reply) {
        String sql = "INSERT INTO reply (content, written_at, writer_id, post_id, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, reply.getContent(), reply.getWrittenAt(), reply.getWriterId(), reply.getPostId(), reply.getStatus().name());
    }
}
