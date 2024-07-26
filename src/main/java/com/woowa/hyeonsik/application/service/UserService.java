package com.woowa.hyeonsik.application.service;

import com.woowa.hyeonsik.application.dao.UserDao;
import com.woowa.hyeonsik.application.domain.User;

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
        userDao.save(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findByUserId(String id) {
        return userDao.findByUserId(id)
            .orElseThrow(IllegalArgumentException::new);
    }

    public void updateUser(User newUser) {
        User originUser = findByUserId(newUser.getUserId());
        if (!originUser.getPassword().equals(newUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userDao.update(newUser);
    }
}
