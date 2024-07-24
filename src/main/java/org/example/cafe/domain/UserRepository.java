package org.example.cafe.domain;

import java.util.List;

public interface UserRepository {

    void save(User user);

    User findById(String id);

    List<User> findAll();

    void deleteAll();
}
