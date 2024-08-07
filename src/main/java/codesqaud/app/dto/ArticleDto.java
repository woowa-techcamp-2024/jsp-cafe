package codesqaud.app.dto;

import codesqaud.app.model.Reply;

import java.util.List;
import java.util.Objects;

public class ArticleDto {
    private final Long id;
    private final String title;
    private final String contents;
    private final String createdAt;
    private final UserDto author;
    private final Boolean activate;
    private final Long replyCount;
    private boolean isMine;

    public ArticleDto(Long id, String title, String contents, String createdAt, UserDto author, Long replyCount, Boolean activate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.author = author;
        this.replyCount = replyCount;
        this.activate = activate;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public Boolean getActivate() {
        return activate;
    }

    public UserDto getAuthor() {
        return author;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public Long getReplyCount() {
        return replyCount;
    }

    public void setMine(Long loginUserId) {
        this.isMine = loginUserId.equals(author.getId());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String title;
        private String contents;
        private String createdAt;
        private UserDto author;
        private long replyCount;
        private Boolean activate;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder activate(Boolean activate) {
            this.activate = activate;
            return this;
        }

        public Builder author(UserDto author) {
            this.author = author;
            return this;
        }

        public Builder replyCount(long replyCount) {
            this.replyCount = replyCount;
            return this;
        }

        public ArticleDto build() {
            return new ArticleDto(id, title, contents, createdAt, author, replyCount, activate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
