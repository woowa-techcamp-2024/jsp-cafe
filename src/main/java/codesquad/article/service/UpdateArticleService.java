package codesquad.article.service;

import codesquad.article.domain.Article;
import codesquad.article.repository.ArticleRepository;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;

import java.util.Optional;

public class UpdateArticleService {
    private final ArticleRepository repository;

    public UpdateArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public void update(Command cmd) throws NoSuchElementException, UnauthorizedRequestException {
        Optional<Article> findArticle = repository.findById(cmd.articleId());
        if (findArticle.isEmpty()) {
            throw new NoSuchElementException();
        }
        Article article = findArticle.get();
        article.update(cmd.userId(), cmd.content());
        repository.update(article);
    }

    public record Command(
            Long articleId,
            String content,
            String userId
    ) {
    }
}
