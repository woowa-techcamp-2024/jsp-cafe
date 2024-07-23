package org.example.jspcafe.user.service;


import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.UserRepository;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        userRepository.save(user);
    }
}