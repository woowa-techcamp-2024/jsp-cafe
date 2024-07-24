package org.example.demo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserDb {
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static Optional<User> getUser(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static void addUser(User user) {
        users.put(user.getId(), user);
    }

    public static List<User> getUsers() {
        return users.values().stream().toList();
    }
}
