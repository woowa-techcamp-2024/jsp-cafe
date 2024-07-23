package com.woowa.database;

import com.woowa.model.Question;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuestionMemoryDatabase implements QuestionDatabase {

    private final Map<String, Question> questions = new ConcurrentHashMap<>();

    @Override
    public void save(Question question) {
        questions.put(question.getQuestionId(), question);
    }

    @Override
    public List<Question> findAll() {
        return questions.values().stream().toList();
    }
}
