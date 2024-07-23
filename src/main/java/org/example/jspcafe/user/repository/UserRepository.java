package org.example.jspcafe.user.repository;

import org.example.jspcafe.user.User;

import java.util.List;

public interface UserRepository {
    Long save(User user);

    List<User> getAll();
}
