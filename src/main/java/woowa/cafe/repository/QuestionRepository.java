package woowa.cafe.repository;

import woowa.cafe.domain.Question;
import woowa.cafe.dto.Pageable;

import java.util.List;

public interface QuestionRepository {

    void save(Question question);

    List<Question> findAll(Pageable pageable);

    Question findById(String id);

    void update(Question question);

    void deleteById(String id);

    long count();
}
