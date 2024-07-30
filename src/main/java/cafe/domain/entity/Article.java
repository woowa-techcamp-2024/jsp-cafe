package cafe.domain.entity;

import java.time.LocalDateTime;

public class Article {
    private final String articleId;
    private final String writer;
    private final String title;
    private final String contents;
    private final String created;

    public static Article of(String articleId, String writer, String title, String contents) {
        return new Article(articleId, writer, title, contents, LocalDateTime.now().toString());
    }

    private Article(String articleId, String writer, String title, String contents, String created) {
        validate(articleId, writer, title, contents);
        this.articleId = articleId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.created = created;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCreated() {
        return created;
    }

    private void validateArticleId(String articleId) {
        if (articleId == null || articleId.isEmpty()) {
            throw new IllegalArgumentException("ArticleId is empty!");
        }
    }

    private void validateWriter(String writer) {
        if (writer == null || writer.isEmpty()) {
            throw new IllegalArgumentException("Writer is empty!");
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title is empty!");
        }
    }

    private void validateContents(String contents) {
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("Contents is empty!");
        }
    }

    private void validate(String articleId, String writer, String title, String contents) {
        validateArticleId(articleId);
        validateWriter(writer);
        validateTitle(title);
        validateContents(contents);
    }
}
