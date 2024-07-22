package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemUserRepository implements UserRepository {
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    @Override
    public void save(String userId, String password, String name, String email) {
        users.put(userId, new User(userId, password, name, email));
    }

    @Override
    public User findById(String userId) {
        return users.get(userId);
    }


}
