package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Long generateId() {
        return idGenerator.getAndIncrement();
    }

    public void save(User user) {
        store.put(user.getId(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<User> findById(final Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void update(final User user) {
        store.put(user.getId(), user);
    }
}
