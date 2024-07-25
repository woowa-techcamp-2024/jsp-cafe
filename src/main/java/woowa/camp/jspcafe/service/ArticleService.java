package woowa.camp.jspcafe.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.repository.ArticleRepository;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.ArticleResponse;
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

}
