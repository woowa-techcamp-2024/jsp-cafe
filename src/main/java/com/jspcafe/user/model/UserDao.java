package com.jspcafe.user.model;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    private final Map<String, String> idByEmail = new HashMap<>();
    private final Map<String, User> usersById = new HashMap<>();

    public void save(User user) {
        usersById.put(user.id(), user);
        idByEmail.put(user.email(), user.id());
    }

    public User findById(String id) {
        return usersById.get(id);
    }

    public User findByEmail(String email) {
        String id = idByEmail.get(email);
        return findById(id);
    }
}
