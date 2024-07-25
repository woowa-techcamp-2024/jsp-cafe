package org.example.cafe.domain;

import java.util.List;

public interface QuestionRepository {

    Long save(Question question);

    Question findById(Long id);

    List<Question> findAll();

    void deleteAll();
}
