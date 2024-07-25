package org.example.data;

import org.example.domain.User;

import java.util.List;

public interface UserDataHandler {
    User insert(User user);
    User update(User user);
    User findByUserId(Long userId);
    List<User> findAll();
}
