package org.example.jspcafe.user.repository;

import org.example.jspcafe.Repository;
import org.example.jspcafe.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    List<User> findAll();
    List<User> findAllById(Collection<Long> collectUserId);

    void deleteAllInBatch();
}
