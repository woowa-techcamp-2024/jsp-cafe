package com.wootecam.jspcafe.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);

    List<User> findAllOrderByIdDesc();

    Optional<User> findById(final Long id);

    void update(final User user);

    Optional<User> findByUserId(String userId);
}
