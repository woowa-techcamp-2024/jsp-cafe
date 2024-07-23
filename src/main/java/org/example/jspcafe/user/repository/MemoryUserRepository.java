package org.example.jspcafe.user.repository;

import org.example.jspcafe.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserRepository implements UserRepository{
    public static ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    static AtomicLong counter = new AtomicLong();

    public static MemoryUserRepository memoryUserRepository = new MemoryUserRepository();

    @Override
    public Long save(User user) {
        long id = counter.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(users.get(id));
    }
}
