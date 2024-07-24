package cafe.domain.db;

import cafe.domain.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDatabase {
    private final Map<String, User> userDatabase;

    public UserDatabase() {
        this.userDatabase = new HashMap<>();
    }

    public void save(User user) {
        userDatabase.put(UUID.randomUUID().toString(), user);
    }

    public User find(String id) {
        if (!userDatabase.containsKey(id)) {
            return null;
        }
        return userDatabase.get(id);
    }

    public Map<String, User> findAll() {
        return userDatabase;
    }

    public void update(String id, User user) {
        userDatabase.put(id, user);
    }
}
