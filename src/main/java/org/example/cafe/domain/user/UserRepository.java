package org.example.cafe.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private static final Map<String, User> storage = new ConcurrentHashMap<>();

    static {
        storage.put("javajigi", new User("javajigi", "javajigi", "자바지기", "javajigi@sample.net"));
        storage.put("slipp", new User("slipp", "slipp", "슬립", "slipp@sample.net"));
    }

    public void save(User user) {
        storage.put(user.getUserId(), user);
    }

    public User findById(String id) {
        return storage.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }
}
