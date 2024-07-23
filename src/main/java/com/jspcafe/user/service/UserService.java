package com.jspcafe.user.service;

import com.jspcafe.exception.UserNotFoundException;
import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public void signUp(final String email, final String nickname, final String password) {
        userDao.save(User.create(email, nickname, password));
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findById(final String id) {
        return userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User id not found: id = " + id));
    }

    public void update(final User currentUser, final String email, final String nickname, final String password) {
        User updateUser = currentUser.update(email, nickname, password);
        userDao.update(updateUser);
    }
}
