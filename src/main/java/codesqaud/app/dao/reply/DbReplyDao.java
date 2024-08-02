package codesqaud.app.dao.reply;

import codesqaud.app.db.JdbcTemplate;
import codesqaud.app.db.RowMapper;
import codesqaud.app.dto.ReplyDto;
import codesqaud.app.dto.UserDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static codesqaud.app.util.TimeUtils.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class DbReplyDao implements ReplyDao {
    private static final Logger log = LoggerFactory.getLogger(DbReplyDao.class);

    private static final RowMapper<Reply> REPLY_ROW_MAPPER = (resultSet) -> Reply.builder()
            .id(resultSet.getLong("id"))
            .contents(resultSet.getString("contents"))
            .articleId(resultSet.getLong("article_id"))
            .authorId(resultSet.getLong("author_id"))
            .createdAt(toOffsetDateTime(resultSet.getTimestamp("created_at")))
            .activate(resultSet.getBoolean("activate"))
            .build();

    private static final RowMapper<ReplyDto> REPLY_DTO_ROW_MAPPER = (resultSet) -> ReplyDto.builder()
            .id(resultSet.getLong("id"))
            .contents(resultSet.getString("contents"))
            .createdAt(toStringForUser(resultSet.getTimestamp("created_at")))
            .activate(resultSet.getBoolean("activate"))
            .author(new UserDto(
                    resultSet.getLong("author_id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_email")
            ))
            .build();

    private final JdbcTemplate jdbcTemplate;

    public DbReplyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Reply reply) {
        if (reply.getId() != null) {
            log.error("새로운 모델을 저장할 때 id를 명시적으로 지정하면 안됩니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "INSERT INTO replies (contents, article_id, author_id, activate, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reply.getContents(),
                reply.getArticleId(),
                reply.getAuthorId(),
                reply.getActivate(),
                toStringForQuery(reply.getCreatedAt())
        );
    }

    @Override
    public void update(Reply reply) {
        if (reply.getId() == null) {
            log.error("업데이트할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "UPDATE replies SET contents = ? WHERE id = ? AND activate = true";
        int updateRow = jdbcTemplate.update(sql, reply.getContents(), reply.getId());
        if (updateRow == 0) {
            throw new HttpException(SC_NOT_FOUND, "업데이트 할 댓글을 찾지 못했습니다.");
        }
    }

    @Override
    public Optional<Reply> findById(Long id) {
        String sql = "SELECT * FROM replies WHERE id = ? AND activate = true";
        Reply reply = jdbcTemplate.queryForObject(sql, REPLY_ROW_MAPPER, id);
        return Optional.ofNullable(reply);
    }

    @Override
    public List<Reply> findAll() {
        String sql = "SELECT * FROM replies WHERE activate = true";
        return jdbcTemplate.query(sql, REPLY_ROW_MAPPER);
    }

    @Override
    public void delete(Reply reply) {
        String sql = "UPDATE replies SET activate = false WHERE id = ?";
        int update = jdbcTemplate.update(sql, reply.getId());

        if (update == 0) {
            throw new HttpException(SC_NOT_FOUND, "해당 댓글은 존재하지 않습니다.");
        }
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        String sql = """
                SELECT * FROM replies
                WHERE article_id = ? AND activate = true
                """;

        return jdbcTemplate.query(sql, REPLY_ROW_MAPPER, articleId);
    }

    @Override
    public List<ReplyDto> findByArticleIdAsDto(Long articleId) {
        String sql = """
                SELECT replies.id, replies.contents, replies.author_id, replies.created_at, replies.activate,
                users.user_id as user_id, users.name as user_name, users.email as user_email
                FROM replies JOIN users ON replies.author_id = users.id
                WHERE replies.article_id = ? AND replies.activate = true
                """;

        return jdbcTemplate.query(sql, REPLY_DTO_ROW_MAPPER, articleId);
    }
}
