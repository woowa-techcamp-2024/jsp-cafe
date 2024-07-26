package service;

import domain.User;
import exception.TomcatException;
import repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeProfile(String userId, String newPassword, String name, String email, String password) {
        userRepository.findByUserId(userId)
                .ifPresentOrElse(
                        u -> {
                            if (u.getPassword().equals(password)) {
                                u.setPassword(newPassword);
                                u.setName(name);
                                u.setEmail(email);
                                return;
                            }
                            throw new TomcatException("Password is incorrect");
                        },
                        () -> {
                            throw new TomcatException("User not found");
                        });
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new TomcatException("User not found"));
    }


}
