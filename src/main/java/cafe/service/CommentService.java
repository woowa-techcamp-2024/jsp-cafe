package cafe.service;

import cafe.domain.db.CommentDatabase;

public class CommentService {
    private final CommentDatabase commentDatabase;

    public CommentService(CommentDatabase commentDatabase) {
        this.commentDatabase = commentDatabase;
    }
}
