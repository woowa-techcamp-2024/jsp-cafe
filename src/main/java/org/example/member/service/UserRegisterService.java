package org.example.member.service;

import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserRegisterResponseDto;
import org.example.member.repository.UserRepository;

public class UserRegisterService {

    private final UserRepository userRepository = new UserRepository();

    public UserRegisterResponseDto register(User user) throws SQLException {
        return userRepository.register(user);
    }
}
