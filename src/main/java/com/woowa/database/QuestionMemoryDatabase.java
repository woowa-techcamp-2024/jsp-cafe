package com.woowa.database;

import com.woowa.model.Question;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Override
    public List<Question> findAllOrderByCreatedAt(int page, int size) {
        return questions.values().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit((long) page * size + size).skip((long) page * size)
                .toList();
    }

    @Override
    public Optional<Question> findById(String questionId) {
        return Optional.ofNullable(questions.get(questionId));
    }

    @Override
    public void update(Question question) {

    }

    @Override
    public void delete(Question question) {
        questions.remove(question.getQuestionId());
    }

    @Override
    public Optional<Question> findByIdWithReplies(String questionId) {
        return findById(questionId);
    }
}