package org.example.jspcafe.user.service;


import org.example.jspcafe.user.User;
import org.example.jspcafe.user.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        Long saved = userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public void updateUser(Long id, String nickname, String password, String email) {
        User user = findById(id);
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Passwords don't match");
        }
        user.setNickname(nickname);
        user.setEmail(email);

        userRepository.update(user);
    }

    public User findByIdAndPw(String userId, String password) {
        User user = userRepository.findByUserId(userId).orElseThrow(IllegalArgumentException::new);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }
}