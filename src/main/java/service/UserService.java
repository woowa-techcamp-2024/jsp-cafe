package service;

import domain.User;
import dto.UsersDao;
import exception.TomcatException;
import repository.users.UserRepository;

import java.util.List;
import java.util.Optional;

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
                                userRepository.updateUser(u);
                                return;
                            }
                            throw new TomcatException("Password is incorrect");
                        },
                        () -> {
                            throw new TomcatException("User not found");
                        });
    }

    public boolean isValidUser(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(UsersDao userDao) {
        User user = new User(null, userDao.getUserId(), userDao.getPassword(), userDao.getName(), userDao.getEmail());
        userRepository.saveUser(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new TomcatException("User not found"));
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new TomcatException("User not found"));
    }

}
