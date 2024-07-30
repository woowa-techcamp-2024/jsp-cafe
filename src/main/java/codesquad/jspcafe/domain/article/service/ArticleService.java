package codesquad.jspcafe.domain.article.service;

import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.request.ArticleUpdateRequest;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

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
        Article article = findArticleById(Long.parseLong(id));
        return ArticleCommonResponse.from(article);
    }

    public List<ArticleContentResponse> findAllArticle() {
        return articleRepository.findAll().stream().map(ArticleContentResponse::from).toList();
    }

    public ArticleCommonResponse updateArticle(ArticleUpdateRequest request)
        throws AccessDeniedException {
        Article article = findArticleById(request.getArticleId());
        if (!Objects.equals(article.getWriter().getId(), request.getUserId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        article.updateValues(request.getTitle(), request.getContents());
        return ArticleCommonResponse.from(articleRepository.update(article));
    }

    public void deleteArticle(Long articleId, Long userId) throws AccessDeniedException {
        Article article = findArticleById(articleId);
        if (!Objects.equals(article.getWriter().getId(), userId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        articleRepository.delete(article);
    }

    private Article findArticleById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("아티클이 존재하지 않습니다."));
    }
}
