package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.domain.UserRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(final String userId, final String password, final String name, final String email) {
        User user = new User(userId, password, name, email);

        log.info("signUpUser = {}", user);

        userRepository.save(user);
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public User read(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. input path = " + id));
    }

    public void edit(final Long id,
                     final String originalPassword,
                     final String newPassword,
                     final String name,
                     final String email) {
        User user = read(id);
        User editedUser = user.edit(originalPassword, newPassword, name, email);

        userRepository.update(editedUser);
    }
}
