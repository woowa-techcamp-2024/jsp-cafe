package org.example.jspcafe.user.repository;

import org.example.jspcafe.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserRepository implements UserRepository{
    static public ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    static AtomicLong counter = new AtomicLong();

    @Override
    public Long save(User user) {
        long id = counter.incrementAndGet();
        users.put(id, user);
        return id;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
