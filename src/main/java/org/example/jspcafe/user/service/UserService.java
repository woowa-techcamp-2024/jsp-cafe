package org.example.jspcafe.user.service;


import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.MemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public static UserService userService = new UserService(MemoryUserRepository.memoryUserRepository);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        Long saved = userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}