package codesqaud.app.dao;

import codesqaud.app.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserDao implements UserDao {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    
    @Override
    public void save(User user) {
        users.compute(user.getUserId(), (userId, existingUser) -> {
            if (existingUser != null) {
                throw new IllegalStateException("이미 존재하는 사용자 입니다");
            }
            return user;
        });
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(User user) {
    }
}
