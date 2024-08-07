package codesquad.jspcafe.domain.article.service;

import codesquad.jspcafe.common.payload.response.CursorPaginationResult;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.request.ArticleUpdateRequest;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.reply.repository.ReplyRepository;
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
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, ReplyRepository replyRepository,
        UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.replyRepository = replyRepository;
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

    public CursorPaginationResult<ArticleContentResponse> getArticlesByPage(int page) {
        List<ArticleContentResponse> result = articleRepository.findByPage(page, 15)
            .stream().map(ArticleContentResponse::from).toList();
        return CursorPaginationResult.of(result, 15);
    }

    public Long getTotalArticlesCount() {
        return articleRepository.count();
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
        List<Reply> replies = replyRepository.findByArticleId(articleId);
        if (!replies.stream().allMatch(reply -> reply.getWriter().getId().equals(userId))) {
            throw new IllegalArgumentException("댓글이 존재하여 삭제할 수 없습니다.");
        }
        replies.forEach(replyRepository::delete);
        articleRepository.delete(article);
    }

    private Article findArticleById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("아티클이 존재하지 않습니다."));
    }
}
