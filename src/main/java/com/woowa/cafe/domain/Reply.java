package com.woowa.cafe.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reply {

    private Long replyId;
    private Long articleId;
    private String writerId;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Reply(final Long replyId, final Long articleId,
                 final String writerId, final String contents,
                 final LocalDateTime createAt, final LocalDateTime updateAt) {
        this.replyId = replyId;
        this.articleId = articleId;
        this.writerId = writerId;
        this.contents = contents;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Reply(final Long articleId, final String writerId, final String contents) {
        this.contents = contents;
        this.writerId = writerId;
        this.articleId = articleId;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    public void setId(final Long replyId) {
        this.replyId = replyId;
    }

    public Long getId() {
        return replyId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getUpdatedAt() {
        return updateAt;
    }

    public LocalDateTime getCreatedAt() {
        return createAt;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(createAt);
    }

    public String getFormattedUpdatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(updateAt);
    }

    public void update(final String contents) {
        this.contents = contents;
        this.updateAt = LocalDateTime.now();
    }

    public boolean isSameWriter(final String memberId) {
        return writerId.equals(memberId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Reply reply)) return false;
        return Objects.equals(replyId, reply.replyId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(replyId);
    }
}
