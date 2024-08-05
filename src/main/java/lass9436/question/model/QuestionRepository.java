package lass9436.question.model;

import java.util.List;

public interface QuestionRepository {
	Question save(Question question); // Create or Update

	Question findByQuestionSeq(long questionSeq); // Read by questionSeq

	List<Question> findAll(); // Read all

	void deleteByQuestionSeq(long questionSeq); // Delete by questionSeq

	Question findByTitle(String title); // Read by title

	List<Question> findAllPageable(long page, long pageSize);

	long count();
}
