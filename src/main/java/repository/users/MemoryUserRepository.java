package repository.users;

import domain.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> userMap;
    private static final AtomicLong sequence = new AtomicLong();

    public MemoryUserRepository(Map<Long, User> map) {
        this.userMap = map;
    }

    @Override
    public void saveUser(User user) {
        user.setId(sequence.incrementAndGet());
        if (user.getUserId().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getEmail().isEmpty()) {
            return;
        }
        userMap.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void updateUser(User user) {
        userMap.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userMap.values().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }



}