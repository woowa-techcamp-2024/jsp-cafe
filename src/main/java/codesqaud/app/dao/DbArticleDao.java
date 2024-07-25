package codesqaud.app.dao;

import codesqaud.app.model.Article;

import java.util.List;
import java.util.Optional;

public class DbArticleDao implements ArticleDao {
    private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (resultSet) -> new Article(
            resultSet.getLong("id"),
            resultSet.getString("title"),
            resultSet.getString("contents"),
            resultSet.getString("author_id")
    );

    private final JdbcTemplate jdbcTemplate;

    public DbArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Article article) {
        String sql = "INSERT INTO articles (title, contents, author_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, article.getTitle(), article.getContents(), article.getAuthorId());
    }

    @Override
    public void update(Article article) {
        String sql = "UPDATE articles SET title = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, article.getTitle(), article.getContents(), article.getId());
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
        jdbcTemplate.update(sql, article.getId());
    }
}
