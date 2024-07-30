package codesquad.jspcafe.domain.reply.payload.request;

public class ReplyUpdateRequest {

    private final Long id;
    private final String userId;
    private final String contents;

    public ReplyUpdateRequest(Long id, String userId, String contents) {
        this.id = id;
        this.userId = userId;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getContents() {
        return contents;
    }

}
