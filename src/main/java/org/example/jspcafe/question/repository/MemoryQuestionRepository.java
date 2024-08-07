package org.example.jspcafe.question.repository;

import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.QuestionPagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryQuestionRepository implements QuestionRepository{

    public static ConcurrentHashMap<Long, Question> questions = new ConcurrentHashMap<>();
    static AtomicLong counter = new AtomicLong();
    @Override
    public Long save(Question question) {
        long id = counter.incrementAndGet();
        question.setId(id);
        questions.put(id, question);
        return id;
    }

    @Override
    public List<Question> getAll() {
        return new ArrayList<>(questions.values());
    }

    @Override
    public Optional<Question> findById(Long id) {
        return Optional.of(questions.get(id));
    }

    @Override
    public boolean update(Question question) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public QuestionPagination getAllWithPagination(int page, int pageSize) {
        return null;
    }
}
