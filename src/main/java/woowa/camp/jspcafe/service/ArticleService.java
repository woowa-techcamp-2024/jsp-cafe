package woowa.camp.jspcafe.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.dto.request.ArticleUpdateRequest;
import woowa.camp.jspcafe.repository.reply.ReplyRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.request.ArticleWriteRequest;
import woowa.camp.jspcafe.service.dto.response.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.response.ArticlePreviewResponse;
import woowa.camp.jspcafe.service.dto.response.ArticleUpdateResponse;

public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final DateTimeProvider dateTimeProvider;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository,
                          ReplyRepository replyRepository, DateTimeProvider dateTimeProvider
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
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

    public List<ArticlePreviewResponse> findArticleList(long page) {
        long offset = (page - 1) * PageVO.MAX_ROW_COUNT_PER_PAGE;
        List<Article> articles = articleRepository.findByOffsetPagination(offset, PageVO.MAX_ROW_COUNT_PER_PAGE);

        List<ArticlePreviewResponse> articlePreviewRespons = new ArrayList<>();
        for (Article article : articles) {
            User author = findAuthor(article.getAuthorId());
            articlePreviewRespons.add(ArticlePreviewResponse.of(article, author.getId(), author.getNickname()));
        }

        return articlePreviewRespons;
    }

    public Long findTotalArticleCounts() {
        return articleRepository.findAllArticleCounts();
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
        Article updateArticle = Article.update(article, article.getTitle(), article.getContent(),
                article.getUpdatedAt());
        articleRepository.update(updateArticle);
    }

    public ArticleUpdateResponse findUpdateArticle(User user, Long articleId) {
        Article article = findArticle(articleId);
        User author = findAuthor(article.getAuthorId());
        validateEditable(user, author);

        return ArticleUpdateResponse.from(article);
    }

    public void updateArticle(User user, Long articleId, ArticleUpdateRequest request) {
        Article article = findArticle(articleId);
        User author = findAuthor(article.getAuthorId());
        validateEditable(user, author);

        Article updateArticle = Article.update(article, request.title(), request.content(), dateTimeProvider.getNow());
        articleRepository.update(updateArticle);
    }

    /*
     * 요구사항
     * - 댓글이 없는 경우 삭제가 가능하다.
     * - 게시글에 다른 사람이 댓글을 작성한 경우, 게시글 삭제가 불가능하다.
     * - 자기 자신만 댓글을 작성한 경우만, 게시글 삭제가 가능하다.
     * - 이 때 게시글을 삭제할 경우 자기가 쓴 댓글도 모두 삭제된다.
     */
    public void deleteArticle(User user, Long articleId) {
        Article article = findArticle(articleId);
        User author = findAuthor(article.getAuthorId());
        validateEditable(user, author);

        if (isExistNotAuthorReply(article)) {
            throw new ArticleException("다른 사람의 댓글이 있는 게시글은 삭제할 수 없습니다.");
        }

        LocalDate deletedTime = dateTimeProvider.getNow();
        articleRepository.softDeleteById(articleId, deletedTime);
        replyRepository.softDeleteByArticleId(articleId, deletedTime.atStartOfDay());
    }

    // FIXME: 대규모 데이터 고려 필요
    private boolean isExistNotAuthorReply(Article article) {
        List<Reply> replies = replyRepository.findByArticleId(article.getId());
        return replies.stream()
                .anyMatch(reply -> !reply.getUserId().equals(article.getAuthorId()));
    }

    private void validateEditable(User user, User author) {
        String userEmail = user.getEmail();
        String authorEmail = author.getEmail();
        if (!userEmail.equals(authorEmail)) {
            throw new UnAuthorizationException("게시글 수정은 작성자의 이메일과 동일해야 합니다. %s, %s".formatted(userEmail, authorEmail));
        }
    }

}
