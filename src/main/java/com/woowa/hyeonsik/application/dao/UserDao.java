package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    void update(User user);
    void removeByUserId(String userId);
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    List<User> findAll();
    void clear();
}
