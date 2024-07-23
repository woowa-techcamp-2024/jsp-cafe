package codesquad.jspcafe.domain.article.domain;

public class Article {

    private Long id;
    private final String title;
    private final String writer;
    private final String contents;

    public Article(String title, String writer, String contents) {
        this.title = verifyTitle(title);
        this.writer = verifyWriter(writer);
        this.contents = verifyContents(contents);
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    private String verifyTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수 입력값입니다.");
        }
        return title;
    }

    private String verifyWriter(String writer) {
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException("글쓴이는 필수 입력값입니다.");
        }
        return writer;
    }

    private String verifyContents(String contents) {
        if (contents == null || contents.isBlank()) {
            throw new IllegalArgumentException("본문은 필수 입력값입니다.");
        }
        return contents;
    }

}
