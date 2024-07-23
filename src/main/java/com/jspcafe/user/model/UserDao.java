package com.jspcafe.user.model;

import java.util.*;

public class UserDao {
    private final Map<String, String> idByEmail = new HashMap<>();
    private final Map<String, User> usersById = new HashMap<>();

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
}
