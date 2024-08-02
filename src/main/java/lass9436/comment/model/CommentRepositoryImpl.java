package lass9436.comment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommentRepositoryImpl implements CommentRepository {
	private final Map<Long, Comment> commentStore = new ConcurrentHashMap<>();
	private long sequence = 0L;

	@Override
	public Comment save(Comment comment) {
		if (comment.getCommentSeq() == 0) {
			comment.setCommentSeq(++sequence);
		}
		commentStore.put(comment.getCommentSeq(), comment);
		return comment;
	}

	@Override
	public Comment findByCommentSeq(long commentSeq) {
		return commentStore.get(commentSeq);
	}

	@Override
	public List<Comment> findAll() {
		return new ArrayList<>(commentStore.values());
	}

	@Override
	public void deleteByCommentSeq(long commentSeq) {
		commentStore.remove(commentSeq);
	}

	@Override
	public List<Comment> findByQuestionSeq(long questionSeq) {
		List<Comment> comments = new ArrayList<>();
		for (Comment comment : commentStore.values()) {
			if (comment.getQuestionSeq() == questionSeq) {
				comments.add(comment);
			}
		}
		return comments;
	}
}

