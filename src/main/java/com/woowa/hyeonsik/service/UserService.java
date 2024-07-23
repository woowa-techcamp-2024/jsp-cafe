package com.woowa.hyeonsik.service;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.domain.User;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // addUser
    public void add(User user) {
        userDao.add(user);
    }

    // findAllUser


    // findById

}
