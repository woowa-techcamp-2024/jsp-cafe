package org.example.cafe.application;

import org.example.cafe.application.dto.LoginDto;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(LoginDto loginDto) {
        User user = userRepository.findById(loginDto.userId());

        return user != null && user.matchPassword(loginDto.password());
    }
}
