package codesquad.comment.service;

import codesquad.comment.domain.Comment;
import codesquad.comment.repository.CommentRepository;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;

import java.util.Optional;

public class DeleteCommentService {
    private final CommentRepository repository;

    public DeleteCommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public void delete(Command cmd) throws NoSuchElementException, UnauthorizedRequestException {
        Optional<Comment> findComment = repository.findById(cmd.commentId());
        if (findComment.isEmpty()) {
            throw new NoSuchElementException();
        }
        Comment comment = findComment.get();
        comment.delete(cmd.userId());
        repository.update(comment);
    }

    public record Command(
            long commentId,
            String userId
    ) {
    }
}
