package codesquad.jspcafe.domain.user.repository;

import codesquad.jspcafe.domain.user.domain.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserMemoryRepository implements UserRepository {

    private final Map<String, User> map;

    public UserMemoryRepository() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public User save(User user) {
        if (map.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }
        map.put(user.getUserId(), user);
        return user;
    }

    public User update(User user) {
        map.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(map.get(userId));
    }

    @Override
    public List<User> findAll() {
        return map.values().stream().toList();
    }
}
