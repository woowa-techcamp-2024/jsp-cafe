package com.woowa.database;

import com.woowa.model.User;
import java.util.Optional;

public interface UserDatabase {
    void save(User user);

    Optional<User> findByEmail(String email);
}
