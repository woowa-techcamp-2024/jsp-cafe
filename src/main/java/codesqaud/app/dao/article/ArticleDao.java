package codesqaud.app.dao.article;

import codesqaud.app.dao.CommonDao;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleDao extends CommonDao<Article, Long> {
    Optional<Article> findByIdForUpdate(Long id);
    Optional<ArticleDto> findByIdAsDto(Long id);
    List<ArticleDto> findAllAsDto();

    long count();
    List<ArticleDto> findPage(int page, int size);
}
