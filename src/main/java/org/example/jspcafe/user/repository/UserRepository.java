package org.example.jspcafe.user.repository;

import org.example.jspcafe.Repository;
import org.example.jspcafe.user.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> findByEmail(String email);
}
