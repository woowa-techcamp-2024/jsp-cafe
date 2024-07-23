package repository;

import domain.User;

import java.util.*;

public class UserRepository {

    private final Map<Long, User> userMap;

    public UserRepository(Map<Long,User> map) {
        this.userMap = map;
    }

    public void saveUser(User user) {
        if(user.getUserId().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getEmail().isEmpty()) {
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

}