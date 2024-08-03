package codesquad.comment.service;

import codesquad.common.exception.NoSuchElementException;

/**
 * <h3>댓글 작성을 위한 서비스</h3>
 * 댓글을 작성하기 위해서는 우선 Article의 S-lock을 먼저 취득해야 합니다.
 * 이는 댓글 작성과 게시글 삭제 간의 동시성 문제를 해결하기 위한 것으로 자세한 것은 {@link codesquad.article.service.DeleteArticleService}를 참고바랍니다.
 *
 * @see codesquad.article.service.DeleteArticleService
 */
public interface RegisterCommentService {
    Long register(Command cmd) throws NoSuchElementException;

    record Command(
            Long articleId,
            String writer,
            String content
    ) {
    }
}
