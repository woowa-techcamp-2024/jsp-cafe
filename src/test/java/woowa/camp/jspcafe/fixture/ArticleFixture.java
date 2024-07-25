package woowa.camp.jspcafe.fixture;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.service.dto.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.ArticleWriteRequest;

public class ArticleFixture {

    public static Article createArticle1(LocalDate now) {
        return Article.create(1L, "게시글_제목1", "게시글_내용1", now);
    }

    public static Article createArticle2(LocalDate now) {
        return Article.create(2L, "게시글_제목2", "게시글_내용2", now);
    }

    public static Article createArticle3(LocalDate now) {
        return Article.create(3L, "게시글_제목3", "게시글_내용3", now);
    }

    public static Article createArticleWithAuthorId(Long authorId, LocalDate now) {
        return Article.create(authorId, "게시글_제목_" + authorId, "게시글_내용_" + authorId, now);
    }

    public static List<Article> createMultipleArticles(int count, LocalDate now) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createArticleWithAuthorId((long) i, now))
                .collect(Collectors.toList());
    }

    public static ArticleWriteRequest createArticleWriteRequestWithAuthorId(Long authorId, LocalDate now) {
        return new ArticleWriteRequest(authorId, "게시글_제목_" + authorId, "게시글_내용_" + authorId);
    }

    public static List<ArticleWriteRequest> createMultipleArticleWriteRequests(int count, LocalDate now) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> createArticleWriteRequestWithAuthorId((long) i, now))
                .collect(Collectors.toList());
    }

}
