package com.woowa.database.user;

import com.woowa.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDatabase {
    void save(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    Optional<User> findById(String userId);

    void update(User user);
}
