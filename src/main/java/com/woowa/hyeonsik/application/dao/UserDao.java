package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserDao {
    private static final List<User> users = new CopyOnWriteArrayList<>();

    public void save(User user) {
        if (existsByUserId(user.getUserId())) {
            removeByUserId(user.getUserId());
        }
        users.add(user);
    }

    public void removeByUserId(String userId) {
        Optional<User> first = users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();

        if (!first.isPresent()) {
            return;
        }
        users.remove(first.get());
    }

    public Optional<User> findByUserId(String userId) {
        if (!existsByUserId(userId)) {
            return Optional.empty();
        }

        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

    public boolean existsByUserId(String userId) {
        Optional<User> first = users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();

        return first.isPresent();
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    public void clear() {
        users.clear();
    }
}
