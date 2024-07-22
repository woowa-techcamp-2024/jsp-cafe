package org.example.jspcafe.user.repository;

import org.example.jspcafe.InMemoryRepository;
import org.example.jspcafe.user.model.User;

public class InMemoryUserRepository extends InMemoryRepository<User> {
    public InMemoryUserRepository() {
        super(User.class);
    }
}
