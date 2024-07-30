package woowa.camp.jspcafe.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.dto.ArticleUpdateRequest;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.ArticlePreviewResponse;
import woowa.camp.jspcafe.service.dto.ArticleUpdateResponse;
import woowa.camp.jspcafe.service.dto.ArticleWriteRequest;
import woowa.camp.jspcafe.utils.time.DateTimeProvider;

public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository,
                          DateTimeProvider dateTimeProvider) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public Article writeArticle(ArticleWriteRequest articleWriteRequest) {
        Article article = Article.create(
                articleWriteRequest.authorId(),
                articleWriteRequest.title(),
                articleWriteRequest.content(),
                dateTimeProvider.getNow());

        articleRepository.save(article);
        return article;
    }

    public ArticleDetailsResponse findArticleDetails(Long id) {
        Article article = findArticle(id);
        upHits(article);
        article = findArticle(id);

        User author = findAuthor(article.getAuthorId());
        return ArticleDetailsResponse.of(article, author.getId(), author.getNickname());
    }

    public List<ArticlePreviewResponse> findArticleList(int page) {
        int pageSize = 10;
        int offset = (page - 1) * pageSize;
        List<Article> articles = articleRepository.findByOffsetPagination(offset, pageSize);

        List<ArticlePreviewResponse> articlePreviewRespons = new ArrayList<>();
        for (Article article : articles) {
            User author = findAuthor(article.getAuthorId());
            articlePreviewRespons.add(ArticlePreviewResponse.of(article, author.getId(), author.getNickname()));
        }

        return articlePreviewRespons;
    }

    private Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleException("Article not found : " + id));
    }

    private User findAuthor(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User with id " + userId + " not found"));
    }

    private void upHits(Article article) {
        article.upHits();
        Article updateArticle = Article.update(article, article.getTitle(), article.getContent(), article.getUpdatedAt());
        articleRepository.update(updateArticle);
    }

    public ArticleUpdateResponse findUpdateArticle(User user, Long articleId) {
        Article article = findArticle(articleId);
        User author = findAuthor(article.getAuthorId());
        validateUpdatable(user, author);

        return ArticleUpdateResponse.from(article);
    }

    public void updateArticle(User user, Long articleId, ArticleUpdateRequest request) {
        Article article = findArticle(articleId);
        User author = findAuthor(article.getAuthorId());
        validateUpdatable(user, author);

        Article updateArticle = Article.update(article, request.title(), request.content(), dateTimeProvider.getNow());
        articleRepository.update(updateArticle);
    }

    private void validateUpdatable(User user, User author) {
        String userEmail = user.getEmail();
        String authorEmail = author.getEmail();
        if (!userEmail.equals(authorEmail)) {
            throw new UnAuthorizationException("게시글 수정은 작성자의 이메일과 동일해야 합니다. %s, %s".formatted(userEmail, authorEmail));
        }
    }

}
