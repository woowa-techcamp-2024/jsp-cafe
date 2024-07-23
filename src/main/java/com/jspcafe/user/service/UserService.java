package com.jspcafe.user.service;

import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;

public class UserService {
    private final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public void signUp(final String email, final String nickname, final String password) {
        userDao.save(User.create(email, nickname, password));
    }
}
