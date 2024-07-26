package org.example.mock;

import org.example.data.UserDataHandler;
import org.example.domain.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TestUserDataHandler implements UserDataHandler {
    private Map<Long, User> db = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User insert(User user) {
        user = new User(idGenerator.getAndIncrement(), user.getEmail(), user.getNickname(), user.getPassword(), user.getCreatedDt());
        db.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        db.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findByUserId(Long userId) {
        return db.get(userId);
    }

    @Override
    public User findByEmail(String email) {
        return db.values().stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return db.values().stream().toList();
    }
}
