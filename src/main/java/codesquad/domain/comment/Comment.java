package codesquad.domain.comment;

/**
 * <h3>요구사항</h3>
 * Article은 댓글이 없는 경우 삭제가 가능하다.
 * 게시글 작성자와 댓글 작성자가 다를 경우 삭제는 불가능하다.
 * 단 게시글 작성자와 댓글 작성자가 모두 같은 경우 한 번에 삭제가 가능하다.
 * 이 경우 게시글을 삭제할 때 댓글 또한 삭제해야 하며, 댓글의 삭제 또한 삭제 상태를 변경한다.
 */
public class Comment {
    private Long id;
    private Long articleId;
    private Long writerId;
    private String content;
    private Status status;

    public Comment(Long articleId, Long writerId, String content) {
        this.articleId = articleId;
        this.writerId = writerId;
        this.content = content;
        this.status = Status.COMMENTED;
    }

    public Comment(Long id, Comment comment) {
        this.id = id;
        this.articleId = comment.articleId;
        this.writerId = comment.writerId;
        this.content = comment.content;
        this.status = comment.status;
    }
}
