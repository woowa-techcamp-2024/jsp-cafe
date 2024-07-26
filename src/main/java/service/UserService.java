package service;

import domain.Users;
import dto.UsersDao;
import exception.TomcatException;
import repository.users.UserRepository;

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
                                userRepository.updateUser(u);
                                return;
                            }
                            throw new TomcatException("Password is incorrect");
                        },
                        () -> {
                            throw new TomcatException("User not found");
                        });
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(UsersDao userDao) {
        Users user = new Users(null, userDao.getUserId(), userDao.getPassword(), userDao.getName(), userDao.getEmail());
        userRepository.saveUser(user);
    }

    public Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new TomcatException("User not found"));
    }


}
