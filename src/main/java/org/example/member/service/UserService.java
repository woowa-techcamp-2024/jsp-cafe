package org.example.member.service;

import java.sql.SQLException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserDto;
import org.example.member.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto register(User user) throws SQLException, IllegalArgumentException {
        if (existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("user already exists");
        }
        userValidate(user);
        return UserDto.toResponse(userRepository.save(user));
    }

    public UserDto getUserFromUserId(String userId) throws SQLException, IllegalArgumentException {
        return UserDto.toResponse(userRepository.findUserByUserId(userId));
    }

    public boolean existsByUserId(String userId) throws SQLException {
        return userRepository.existsByUserId(userId);
    }

    public void editUser(String userId, User request) throws SQLException, IllegalArgumentException {
        User user = userRepository.findUserByUserId(userId);
        logger.info("request password: {} / current password: {}", request.getPassword(), user.getPassword());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("password does not match");
        }
        user.changeUserInfo(request.getPassword(), request.getName(), request.getEmail());
        userRepository.update(user);
    }

    public boolean validateUser(String userId, String password) throws SQLException {
        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    private void userValidate(User user) {
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("userId is null");
        }

        if (user.getPassword() == null) {
            throw new IllegalArgumentException("password is null");
        }

        if (user.getName() == null) {
            throw new IllegalArgumentException("name is null");
        }

        if (user.getEmail() == null) {
            throw new IllegalArgumentException("email is null");
        }
    }
}
