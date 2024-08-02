package camp.woowa.jspcafe.model;

public class Reply {
    private final Long id;
    private String content;
    private Long questionId;
    private String writer;
    private final Long writerId;

    public Reply(Long id, String content, Long questionId, String writer, Long writerId) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.writer = writer;
        this.writerId = writerId;
    }

    public Reply(String content, Long questionId, String writer, Long writerId) {
        this(null, content, questionId, writer, writerId);
    }

    public Reply(String content, Long questionId, Long userId) {
        this(null, content, questionId, null, userId);
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
}
