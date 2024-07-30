package codesqaud.app.dto;

public class ArticleDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final UserDto author;

    public ArticleDto(Long id, String title, String contents, UserDto author) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public UserDto getAuthor() {
        return author;
    }
}
