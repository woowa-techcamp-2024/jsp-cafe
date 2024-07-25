package org.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.entity.User;

public class UserRepositoryMemoryImpl implements UserRepository{
    //없어진 user는 null로 처리하기, Get을 하려면 Optional로 받게한다.
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);
    private static UserRepositoryMemoryImpl instance;

    public User saveUser(User user) {
        users.put(index.incrementAndGet(), user);
        return user;
    }

    //삭제가 아니라 null 처리
    public void deleteUser(String userId) {
        users.forEach((key, value) -> {
            if (value != null && value.getUserId().equals(userId)) {
                users.remove(key);
            }
        });
    }


    public Optional<User> getUserByUserId(String userId) {
        return users.values().stream()
            .filter(user -> user.getUserId().equals(userId))
            .findAny();
    }

    public void clear() {
        users.clear();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public static UserRepositoryMemoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryMemoryImpl();
            instance.saveUser(new User("test", "test", "test@naver.com", "test"));
            instance.saveUser(new User("test2", "test2", "test2@naver.com", "test2"));
        }
        return instance;
    }
}
