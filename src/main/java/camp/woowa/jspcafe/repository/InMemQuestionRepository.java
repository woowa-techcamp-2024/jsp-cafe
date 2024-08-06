package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.page.Page;
import camp.woowa.jspcafe.db.page.PageRequest;
import camp.woowa.jspcafe.model.Question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemQuestionRepository implements QuestionRepository {
    private static final AtomicLong sequence_id = new AtomicLong(1L);
    private static final Map<Long, Question> questions = new ConcurrentHashMap<>();

    @Override
    public Long save(Question question) {
        Long id = sequence_id.getAndIncrement();
        questions.put(id, new Question(id, question.getTitle(), question.getContent(), question.getWriter(), question.getWriterId(), LocalDateTime.now()));
        return id;
    }

    @Override
    public List<Question> findAll() {
        return List.copyOf(questions.values());
    }

    @Override
    public Question findById(Long id) {
        return questions.get(id);
    }

    @Override
    public Page<Question> findAllWithPage(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void deleteAll() {
        questions.clear();
        sequence_id.set(1L);
    }

    @Override
    public void update(Question target) {
        questions.put(target.getId(), target);
    }

    @Override
    public void deleteById(Long id) {
        questions.remove(id);
    }
}
