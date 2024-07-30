package org.example.cafe.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;

public class QuestionInMemoryRepository implements QuestionRepository {

    private static final Map<Long, Question> storage = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Long save(Question question) {
        storage.put(question.getQuestionId(), question);
        sequence.addAndGet(1);

        return sequence.get();
    }

    public Question findById(Long id) {
        return storage.get(id);
    }

    public List<Question> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    public void deleteAll() {
        storage.clear();
    }
}
