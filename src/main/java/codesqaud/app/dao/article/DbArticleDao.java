package codesqaud.app.dao.article;

import codesqaud.app.dao.JdbcTemplate;
import codesqaud.app.dao.RowMapper;
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
        String sql = "SELECT * FROM articles WHERE id = ?";
        Article article = jdbcTemplate.queryForObject(sql, ARTICLE_ROW_MAPPER, id);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT * FROM articles";
        return jdbcTemplate.query(sql, ARTICLE_ROW_MAPPER);
    }

    @Override
    public void delete(Article article) {
        String sql = "DELETE FROM articles WHERE id = ?";
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
                WHERE articles.id = ?
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
                """;
        return jdbcTemplate.query(sql, ARTICLE_DTO_ROW_MAPPER);
    }
}
