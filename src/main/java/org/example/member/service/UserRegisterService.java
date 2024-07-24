package org.example.member.service;

import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.repository.UserRepository;

public class UserRegisterService {

    private final UserRepository userRepository = new UserRepository();

    public UserResponseDto register(User user) throws SQLException, IllegalArgumentException {
        if (existsByUserId(user.getUserId())) {
            throw new IllegalArgumentException("user already exists");
        }
        userValidate(user);
        return UserResponseDto.toResponse(userRepository.register(user));
    }

    public boolean existsByUserId(String userId) throws SQLException {
        return userRepository.existsByUserId(userId);
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
