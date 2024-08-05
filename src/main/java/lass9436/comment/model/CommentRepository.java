package lass9436.comment.model;

import java.util.List;

public interface CommentRepository {
	Comment save(Comment comment);

	Comment findByCommentSeq(long commentSeq);

	List<Comment> findAll();

	void deleteByCommentSeq(long commentSeq);

	List<Comment> findByQuestionSeq(long questionSeq);

	List<Comment> findRangeByQuestionSeq(long questionSeq, long startCommentSeq, int count);

	long countByQuestionSeq(long questionSeq);
}
