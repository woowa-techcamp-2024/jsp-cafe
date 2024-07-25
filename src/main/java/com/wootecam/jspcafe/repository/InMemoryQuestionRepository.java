package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryQuestionRepository implements QuestionRepository {

    private final Map<Long, Question> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Long generateId() {
        return idGenerator.getAndIncrement();
    }

    @Override
    public void save(final Question question) {
        store.put(question.getId(), question);
    }

    @Override
    public List<Question> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Question> findById(final Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
