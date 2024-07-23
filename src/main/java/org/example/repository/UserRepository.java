package org.example.repository;

import java.util.Optional;
import org.example.entity.User;

public interface UserRepository {
    User saveUser(User user);
    void deleteUser(Integer userId);
    Optional<User> getUser(Integer userId);
    void clear();
}
