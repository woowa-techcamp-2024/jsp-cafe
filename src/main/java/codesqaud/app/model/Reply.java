package codesqaud.app.model;

import java.time.OffsetDateTime;

public class Reply extends BaseTimeModel {
    private Long id;
    private String contents;
    private Long articleId;
    private Long authorId;
    private Boolean activate = true;

    public Reply(Long id, String contents, Long articleId, Long authorId, Boolean activate, OffsetDateTime createdAt) {
        this.id = id;
        this.contents = contents;
        this.articleId = articleId;
        this.authorId = authorId;
        this.activate = activate;
        this.createdAt = createdAt;
    }

    public Reply(String contents, Long articleId, Long authorId) {
        this.contents = contents;
        this.articleId = articleId;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Boolean getActivate() {
        return activate;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private Long id;
        private String contents;
        private Long articleId;
        private Long authorId;
        private Boolean activate;
        private OffsetDateTime createdAt;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder articleId(Long articleId) {
            this.articleId = articleId;
            return this;
        }

        public Builder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder activate(Boolean activate) {
            this.activate = activate;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Reply build() {
            return new Reply(id, contents, articleId, authorId, activate, createdAt);
        }
    }
}
