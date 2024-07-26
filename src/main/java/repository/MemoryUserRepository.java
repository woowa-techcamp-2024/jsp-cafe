package repository;

import domain.User;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> userMap;

    public MemoryUserRepository(Map<Long, User> map) {
        this.userMap = map;
    }

    public void saveUser(User user) {
        if (user.getUserId().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getEmail().isEmpty()) {
            return;
        }
        userMap.put(user.getId(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public Optional<User> findByUserId(String userId) {
        return userMap.values().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }



}