package codesquad.jspcafe.domain.user.repository;

import codesquad.jspcafe.domain.user.domain.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final Map<String, User> map;

    public UserRepository() {
        map = new ConcurrentHashMap<>();
    }

    public User save(User user) {
        map.put(user.getUserId(), user);
        return user;
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(map.get(userId));
    }

    public List<User> findAll() {
        return map.values().stream().toList();
    }
}
