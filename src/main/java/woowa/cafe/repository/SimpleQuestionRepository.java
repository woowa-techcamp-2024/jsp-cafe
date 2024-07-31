package woowa.cafe.repository;

import woowa.cafe.domain.Question;
import woowa.frame.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleQuestionRepository implements QuestionRepository {
    private Map<String, Question> database = new ConcurrentHashMap<>();

    public void save(Question question) {
        try {
            String uuid = UUID.randomUUID().toString();
            Field id = Question.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(question, uuid);
            database.put(question.getId(), question);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Question> findAll() {
        return database.values().stream().toList();
    }

    public Question findById(String id) {
        return database.get(id);
    }

    @Override
    public void update(Question question) {
        database.put(question.getId(), question);
    }

    @Override
    public void deleteById(String id) {
        database.remove(id);
    }
}
