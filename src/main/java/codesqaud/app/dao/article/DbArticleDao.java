package codesqaud.app.dao.article;

import codesqaud.app.db.JdbcTemplate;
import codesqaud.app.db.RowMapper;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.dto.UserDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static codesqaud.app.util.TimeUtils.*;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class DbArticleDao implements ArticleDao {
    private static final Logger log = LoggerFactory.getLogger(DbArticleDao.class);

    private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (resultSet) -> Article.builder()
            .id(resultSet.getLong("id"))
            .title(resultSet.getString("title"))
            .contents(resultSet.getString("contents"))
            .authorId(resultSet.getLong("author_id"))
            .createdAt(toOffsetDateTime(resultSet.getTimestamp("created_at")))
            .activate(resultSet.getBoolean("activate"))
            .build();

    private static final RowMapper<ArticleDto> ARTICLE_DTO_ROW_MAPPER = (resultSet) -> ArticleDto.builder()
            .id(resultSet.getLong("id"))
            .title(resultSet.getString("title"))
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

    private static final RowMapper<ArticleDto> ARTICLE_PAGE_DTO_ROW_MAPPER = (resultSet) -> ArticleDto.builder()
            .id(resultSet.getLong("id"))
            .title(resultSet.getString("title"))
            .createdAt(toStringForUser(resultSet.getTimestamp("created_at")))
            .activate(resultSet.getBoolean("activate"))
            .author(new UserDto(
                    resultSet.getLong("author_id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_email")
            ))
            .replyCount(resultSet.getLong("reply_count"))
            .build();


    private final JdbcTemplate jdbcTemplate;

    public DbArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Article article) {
        if (article.getId() != null) {
            log.error("새로운 모델을 저장할 때 id를 명시적으로 지정하면 안됩니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "INSERT INTO articles (title, contents, author_id, created_at, activate) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                article.getTitle(),
                article.getContents(),
                article.getAuthorId(),
                toStringForQuery(article.getCreatedAt()),
                article.isActivate()
        );
    }

    @Override
    public void update(Article article) {
        if (article.getId() == null) {
            log.error("업데이트할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "UPDATE articles SET title = ?, contents = ? WHERE id = ?";
        int updateRow = jdbcTemplate.update(sql, article.getTitle(), article.getContents(), article.getId());
        if (updateRow == 0) {
            throw new HttpException(SC_NOT_FOUND, "업데이트 할 qna 글을 찾지 못했습니다.");
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        String sql = """
                SELECT * FROM articles
                WHERE id = ? AND activate = true
                """;

        Article article = jdbcTemplate.queryForObject(sql, ARTICLE_ROW_MAPPER, id);
        return Optional.ofNullable(article);
    }

    @Override
    public Optional<Article> findByIdForUpdate(Long id) {
        String sql = """
                SELECT * FROM articles
                WHERE id = ? AND activate = true
                FOR UPDATE
                """;
        Article article = jdbcTemplate.queryForObject(sql, ARTICLE_ROW_MAPPER, id);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT * FROM articles WHERE activate = true";
        return jdbcTemplate.query(sql, ARTICLE_ROW_MAPPER);
    }

    @Override
    public void delete(Article article) {
        String sql = "UPDATE articles SET activate = false WHERE id = ? AND activate = true";
        int update = jdbcTemplate.update(sql, article.getId());

        if (update == 0) {
            throw new HttpException(SC_NOT_FOUND, "해당 qna 글은 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<ArticleDto> findByIdAsDto(Long id) {
        String sql = """
                SELECT articles.id, articles.title, articles.contents, articles.author_id, articles.created_at, articles.activate,
                users.user_id as user_id, users.name as user_name, users.email as user_email
                FROM articles JOIN users ON articles.author_id = users.id
                WHERE articles.id = ? AND activate = true
                """;

        ArticleDto articleDto = jdbcTemplate.queryForObject(sql, ARTICLE_DTO_ROW_MAPPER, id);
        return Optional.ofNullable(articleDto);
    }

    @Override
    public List<ArticleDto> findAllAsDto() {
        String sql = """
                SELECT articles.id, articles.title, articles.contents, articles.author_id, articles.created_at, articles.activate,
                users.user_id as user_id, users.name as user_name, users.email as user_email
                FROM articles JOIN users ON articles.author_id = users.id
                WHERE activate = true
                ORDER BY id DESC
                """;
        return jdbcTemplate.query(sql, ARTICLE_DTO_ROW_MAPPER);
    }

    @Override
    public long count() {
        String sql = """
                SELECT count(*) FROM articles WHERE activate = true
                """;

        return Long.parseLong(jdbcTemplate.queryForObject(sql).toString());
    }

    @Override
    public List<ArticleDto> findPage(int page, int size) {
        long offset = (long) page * size;
        String sql = """
                SELECT a.id, a.title, a.author_id, a.created_at, a.activate,
                       u.user_id, u.name as user_name, u.email as user_email,
                       COUNT(r.id) as reply_count
                FROM articles as a
                JOIN (
                    SELECT id
                    FROM articles
                    WHERE activate = true
                    ORDER BY id DESC
                    LIMIT ? OFFSET ?
                ) as temp on temp.id = a.id
                JOIN users u ON a.author_id = u.id
                LEFT JOIN replies r ON a.id = r.article_id AND r.activate = true
                GROUP BY a.id
                ORDER BY id DESC
                """;

        return jdbcTemplate.query(sql, ARTICLE_PAGE_DTO_ROW_MAPPER, size, offset);
    }
}
