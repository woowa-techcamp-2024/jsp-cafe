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
	public long countByQuestionSeq(long questionSeq) {
		return 0;
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

	@Override
	public List<Comment> findRangeByQuestionSeq(long questionSeq, long startCommentSeq, int count) {
		List<Comment> result = new ArrayList<>();

		// 모든 댓글을 순회하며 조건에 맞는 댓글을 찾습니다.
		for (Comment comment : commentStore.values()) {
			// 해당 질문의 댓글이고, startCommentSeq보다 작은 commentSeq를 가진 경우
			if (comment.getQuestionSeq() == questionSeq && comment.getCommentSeq() < startCommentSeq) {
				result.add(comment);
			}
		}

		// commentSeq를 기준으로 내림차순 정렬 (최신 댓글이 먼저 오도록)
		result.sort((c1, c2) -> Long.compare(c2.getCommentSeq(), c1.getCommentSeq()));

		// count 개수만큼만 반환
		return result.stream()
				.limit(count)
				.toList();
	}
}

