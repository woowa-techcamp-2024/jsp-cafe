package org.example.jspcafe.user.repository;

import org.example.jspcafe.Component;
import org.example.jspcafe.InMemoryRepository;
import org.example.jspcafe.user.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserRepository extends InMemoryRepository<User> implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(user -> user.getEmail().getValue().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return storage.values().stream()
                .toList();
    }

    public InMemoryUserRepository() {
        super(User.class);
    }
}
