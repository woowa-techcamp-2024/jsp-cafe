package cafe.questions;

public class Reply {
    private final Long id;
    private final Long articleId;
    private final Long userId;
    private final String userName;
    private final String content;

    public Reply(Long articleId, Long userId, String content) {
        this(null, articleId, userId, null, content);
    }

    public Reply(Long id, Long articleId, Long userId, String userName, String content) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
    }

    public Reply withId(Long id) {
        return new Reply(id, articleId, userId, userName, content);
    }

    public Long getId() {
        return id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }
}
