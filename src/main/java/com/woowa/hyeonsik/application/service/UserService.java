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

    /**
     * 계정 정보를 통해 유저 인증 정보를 확인합니다.
     * @param userId
     * @param password
     * @return
     */
    public void validateUser(String userId, String password) {
        final User user = userDao.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}
