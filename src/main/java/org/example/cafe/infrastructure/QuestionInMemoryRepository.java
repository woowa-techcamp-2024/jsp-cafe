package org.example.cafe.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.QuestionRepository;

public class QuestionInMemoryRepository implements QuestionRepository {

    private static final Map<String, Question> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Question question) {
        storage.put(question.getQuestionId(), question);
    }

    public Question findById(String id) {
        return storage.get(id);
    }

    public List<Question> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteAll() {
        storage.clear();
    }
}
