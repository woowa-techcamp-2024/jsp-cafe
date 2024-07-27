package woowa.cafe.repository;

import woowa.cafe.domain.Question;

import java.util.List;

public interface QuestionRepository {

    void save(Question question);

    List<Question> findAll();

    Question findById(String id);
}
