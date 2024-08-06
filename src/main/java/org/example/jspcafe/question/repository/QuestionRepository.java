package org.example.jspcafe.question.repository;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.QuestionPagination;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Long save(Question question);

    List<Question> getAll();

    Optional<Question> findById(Long id);

    boolean update(Question question);

    boolean delete(Long id);

    QuestionPagination getAllWithPagination(int page, int pageSize);
}
