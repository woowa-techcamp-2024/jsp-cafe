package lass9436.question.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionRepositoryImpl implements QuestionRepository {
	private final List<Question> questions = new ArrayList<>();
	private final AtomicLong questionSeqGenerator = new AtomicLong(1);

	@Override
	public Question save(Question question) {
		if (question.getQuestionSeq() == 0) {
			// 새로운 질문일 경우 questionSeq를 자동 증가시킴
			question.setQuestionSeq(questionSeqGenerator.getAndIncrement());
			questions.add(question);
		} else {
			// 기존 질문 업데이트
			questions.stream()
				.filter(q -> q.getQuestionSeq() == question.getQuestionSeq())
				.findFirst()
				.ifPresentOrElse(
					existingQuestion -> questions.set(questions.indexOf(existingQuestion), question),
					() -> questions.add(question)
				);
		}
		return question;
	}

	@Override
	public Question findByQuestionSeq(long questionSeq) {
		return questions.stream()
			.filter(q -> q.getQuestionSeq() == questionSeq)
			.findFirst()
			.orElse(null);
	}

	@Override
	public List<Question> findAll() {
		return new ArrayList<>(questions);
	}

	@Override
	public void deleteByQuestionSeq(long questionSeq) {
		questions.removeIf(q -> q.getQuestionSeq() == questionSeq);
	}

	@Override
	public Question findByTitle(String title) {
		return questions.stream()
			.filter(q -> q.getTitle().equalsIgnoreCase(title))
			.findFirst()
			.orElse(null);
	}

	@Override
	public List<Question> findAllPageable(long page, long pageSize) {
		long startIndex = (page - 1) * pageSize;
		return questions.stream()
				.filter(q -> "Y".equals(q.getUseYn())) // useYn이 'Y'인 질문만 선택
				.sorted((q1, q2) -> Long.compare(q2.getQuestionSeq(), q1.getQuestionSeq())) // questionSeq 내림차순 정렬
				.skip(startIndex)
				.limit(pageSize)
				.toList();
	}

	@Override
	public long count() {
		return questions.stream()
				.filter(q -> "Y".equals(q.getUseYn())) // useYn이 'Y'인 질문만 카운트
				.count();
	}
}
