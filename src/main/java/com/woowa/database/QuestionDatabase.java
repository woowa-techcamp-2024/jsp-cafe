package com.woowa.database;

import com.woowa.model.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionDatabase {
    void save(Question question);

    List<Question> findAll();

    List<Question> findAllOrderByCreatedAt(int page, int size);

    Optional<Question> findById(String questionId);

    void update(Question question);

    void delete(Question question);
}
