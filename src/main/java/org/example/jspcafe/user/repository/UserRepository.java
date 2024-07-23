package org.example.jspcafe.user.repository;

import org.example.jspcafe.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long save(User user);

    List<User> getAll();

    Optional<User> findById(Long id);
}
