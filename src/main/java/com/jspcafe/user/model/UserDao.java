package com.jspcafe.user.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserDao {
    private final Map<String, String> idByEmail = new ConcurrentHashMap<>();
    private final Map<String, User> usersById = new ConcurrentHashMap<>();

    public void save(User user) {
        usersById.put(user.id(), user);
        idByEmail.put(user.email(), user.id());
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(idByEmail.get(email))
                .flatMap(this::findById);
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public void update(final User updateUser) {
        usersById.compute(updateUser.id(), (id, existingUser) -> {
            if (existingUser == null) {
                return null;
            }
            idByEmail.remove(existingUser.email());
            idByEmail.put(updateUser.email(), id);
            return updateUser;
        });
    }
}
