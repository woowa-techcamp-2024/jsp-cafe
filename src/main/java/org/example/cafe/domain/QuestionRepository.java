package org.example.cafe.domain;

import java.util.List;

public interface QuestionRepository {

    Long save(Question question);

    Question findById(Long id);

    List<Question> findAll(Long page, int pageSize);

    Long count(String keyword);

    void update(Question question);

    void delete(Long id);

    void deleteAll();
}
