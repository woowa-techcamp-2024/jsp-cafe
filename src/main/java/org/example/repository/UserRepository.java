package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.User;

public interface UserRepository {
    User saveUser(User user);
    void deleteUser(String userId);
    Optional<User> getUserByUserId(String userId);
    void clear();
    List<User> findAll();
}
