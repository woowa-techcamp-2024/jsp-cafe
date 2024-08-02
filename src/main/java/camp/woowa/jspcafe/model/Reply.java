package camp.woowa.jspcafe.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Reply {
    private final Long id;
    private String content;
    private Long questionId;
    private String writer;
    private final Long writerId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public Reply(Long id, String content, Long questionId, String writer, Long writerId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.writer = writer;
        this.writerId = writerId;
        this.createdAt = createdAt;
    }

    public Reply(String content, Long questionId, String writer, Long writerId) {
        this(null, content, questionId, writer, writerId, null);
    }

    public Reply(String content, Long questionId, Long userId) {
        this(null, content, questionId, null, userId, null);
    }


    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getWriter() {
        return writer;
    }

    public Long getWriterId() {
        return writerId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
