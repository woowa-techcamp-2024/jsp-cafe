package com.codesquad.cafe.db;

import com.codesquad.cafe.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryUserRepository implements UserRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Long, User> users;

    private AtomicLong seq;

    public InMemoryUserRepository() {
        users = new ConcurrentHashMap<>();
        seq = new AtomicLong(1);
    }

    @Override
    public User save(User user) {

        findByUsername(user.getUsername()).ifPresent(existUser -> {
            log.debug("unique constraint violated: {}", user.getUsername());
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        });

        user.setId(seq.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.entrySet().stream()
                .filter(user -> user.getValue().getUsername().equals(username))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    @Override
    public void deleteAll() {
        this.users.clear();
    }
}
