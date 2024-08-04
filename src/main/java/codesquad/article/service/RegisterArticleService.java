package codesquad.article.service;

import codesquad.article.domain.Article;
import codesquad.article.domain.vo.Status;
import codesquad.article.repository.ArticleRepository;

public class RegisterArticleService {
    private final ArticleRepository repository;

    public RegisterArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public Long register(Command cmd) {
        String title = cmd.title();
        String writer = cmd.writer();
        String content = cmd.content();
        return repository.save(new Article(title, writer, content, Status.PUBLISHED));
    }

    public record Command(
            String title,
            String writer,
            String content
    ) {
    }
}
