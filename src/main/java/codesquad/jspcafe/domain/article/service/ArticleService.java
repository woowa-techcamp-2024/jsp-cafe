package codesquad.jspcafe.domain.article.service;

import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ArticleService {

    private transient final ArticleRepository articleRepository;

    public ArticleService() {
        articleRepository = new ArticleRepository();
    }

    public ArticleCommonResponse createArticle(Map<String, String[]> parameterMap) {
        String title = parameterMap.get("title")[0];
        String writer = parameterMap.get("writer")[0];
        String contents = parameterMap.get("contents")[0];
        Article article = new Article(title, writer, contents, LocalDateTime.now());
        return ArticleCommonResponse.from(articleRepository.save(article));
    }

    public ArticleCommonResponse getArticleByTitle(String title) {
        Article article = articleRepository.findByTitle(title)
            .orElseThrow(() -> new IllegalArgumentException("아티클이 존재하지 않습니다!"));
        return ArticleCommonResponse.from(article);
    }

    public List<ArticleContentResponse> findAllArticle() {
        return articleRepository.findAll().stream().map(ArticleContentResponse::from).toList();
    }
}
