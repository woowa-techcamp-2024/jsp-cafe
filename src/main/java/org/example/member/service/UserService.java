package org.example.member.service;

import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto register(User user) throws SQLException, IllegalArgumentException {
        if (existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("user already exists");
        }
        userValidate(user);
        return UserResponseDto.toResponse(userRepository.save(user));
    }

    public boolean existsByUserId(String userId) throws SQLException {
        return userRepository.existsByUserId(userId);
    }

    public void editUser(User request) throws SQLException, IllegalArgumentException {
        User user = userRepository.findUserByUserId(request.getUserId());
        logger.info("request password: {} / current password: {}", request.getPassword(), user.getPassword());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("password does not match");
        }
        user.changeUserInfo(request.getPassword(), request.getName(), request.getEmail());
        userRepository.update(user);
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
