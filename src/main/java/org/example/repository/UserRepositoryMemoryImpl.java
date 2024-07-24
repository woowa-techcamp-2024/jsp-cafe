package org.example.repository;

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
        user.setUserId(index.incrementAndGet());
        users.put(user.getUserId(), user);
        return user;
    }

    //삭제가 아니라 null 처리
    public void deleteUser(Integer userId) {
        // user 목록 출력
        users.remove(userId);
    }

    public Optional<User> getUser(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public void clear() {
        users.clear();
    }

    public UserRepositoryMemoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryMemoryImpl();
        }
        return instance;
    }
}
