package codesquad.jspcafe.domain.article.service;

import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public ArticleCommonResponse createArticle(Map<String, String[]> parameterMap, String userId) {
        String title = parameterMap.get("title")[0];
        String contents = parameterMap.get("contents")[0];
        User writer = userRepository.findByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Article article = new Article(title, writer, contents, LocalDateTime.now());
        return ArticleCommonResponse.from(articleRepository.save(article));
    }

    public ArticleCommonResponse getArticleById(String id) {
        Article article = articleRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new IllegalArgumentException("아티클이 존재하지 않습니다!"));
        return ArticleCommonResponse.from(article);
    }

    public List<ArticleContentResponse> findAllArticle() {
        return articleRepository.findAll().stream().map(ArticleContentResponse::from).toList();
    }
}
