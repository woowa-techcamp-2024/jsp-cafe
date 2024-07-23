package org.example.member.service;

import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.repository.UserRepository;

public class UserRegisterService {

    private final UserRepository userRepository = new UserRepository();

    public UserResponseDto register(User user) throws SQLException {
        return UserResponseDto.toResponse(userRepository.register(user));
    }
}
