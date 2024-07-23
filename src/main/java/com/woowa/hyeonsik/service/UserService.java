package com.woowa.hyeonsik.service;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.domain.User;
import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void signUp(User user) {
        if (userDao.existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("이미 해당 아이디를 사용 중 입니다. ID: " + user.getUserId());
        }
        userDao.add(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findByUserId(String id) {
        return userDao.findByUserId(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
