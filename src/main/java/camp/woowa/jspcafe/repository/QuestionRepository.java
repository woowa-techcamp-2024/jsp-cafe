package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.page.Page;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.model.Question;

import java.util.List;

public interface QuestionRepository {
    Long save(Question question);

    List<Question> findAll();

    Question findById(Long id);

    Page<Question> findAllWithPage(PageRequest pageRequest);

    void deleteAll();

    void update(Question target);

    void deleteById(Long id);
}
