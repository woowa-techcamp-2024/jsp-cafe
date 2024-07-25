package repository;

import domain.Users;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, Users> userMap;

    public MemoryUserRepository(Map<Long, Users> map) {
        this.userMap = map;
    }

    @Override
    public void saveUser(Users user) {
        if (user.getUserId().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getEmail().isEmpty()) {
            return;
        }
        userMap.put(user.getId(), user);
    }

    @Override
    public List<Users> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<Users> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<Users> findByUserId(String userId) {
        return userMap.values().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }



}