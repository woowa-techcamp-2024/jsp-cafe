package cafe.domain.db;

import cafe.domain.entity.User;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private final Map<String, User> userDatabase = new HashMap<>();

    public void save(User user) {
        userDatabase.put(user.getId(), user);
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
}
