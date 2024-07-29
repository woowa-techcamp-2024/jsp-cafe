package codesqaud.app.dao.article;

import codesqaud.app.dao.CommonDao;
import codesqaud.app.dto.ArticleDto;
import codesqaud.app.model.Article;

import java.util.List;

public interface ArticleDao extends CommonDao<Article, Long> {
    List<ArticleDto> findAllAsDto();
}
