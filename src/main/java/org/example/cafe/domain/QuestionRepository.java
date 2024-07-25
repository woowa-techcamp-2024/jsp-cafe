package org.example.cafe.domain;

import java.util.List;

public interface QuestionRepository {

    void save(Question question);

    Question findById(String id);

    List<Question> findAll();

    void deleteAll();
}
