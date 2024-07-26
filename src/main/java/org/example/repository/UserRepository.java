package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.User;

public interface UserRepository {
    User save(User user);
    Optional<User> getUserByUserId(String userId);
    void clear();
    List<User> findAll();
    void updateUser(String userId, String nickname, String email);
}
