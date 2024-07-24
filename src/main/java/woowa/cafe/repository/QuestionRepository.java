package woowa.cafe.repository;

import woowa.cafe.domain.Question;
import woowa.frame.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuestionRepository {

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
}
