package org.example.demo;

import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserDb {
    private static Map<Long, User> users = new ConcurrentHashMap<>();

    public static Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public static void addUser(UserCreateDao dao) {
        User user = new User(generateNxtId(), dao.getUserId(), dao.getPassword(), dao.getName(), dao.getEmail());
        users.put(user.getId(), user);
    }

    public static List<User> getUsers() {
        return users.values().stream().toList();
    }

    private static Long generateNxtId() {
        return (long) users.size();
    }

    public static Optional<User> getUserByUserId(String writer) {
        return users.values().stream()
                .filter(user -> user.getUserId().equals(writer))
                .findAny();
    }

    public static void updateUser(UserUpdateDao dao) {
        //TODO 나중에 user 메소드로 정의하면 좋을 듯?
        //TODO 동시성 문제 고려해보기
        User user = users.get(dao.getId());
        user.setName(dao.getName());
        user.setPassword(dao.getPassword());
        user.setEmail(dao.getEmail());
        users.put(dao.getId(), user);
    }
}
