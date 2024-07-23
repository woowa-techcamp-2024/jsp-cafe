package org.example.cafe.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private static final Map<String, User> storage = new ConcurrentHashMap<>();

    public void save(User user) {
        storage.put(user.getUserId(), user);
    }

    public User findById(String id) {
        return storage.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteAll() {
        storage.clear();
    }
}
