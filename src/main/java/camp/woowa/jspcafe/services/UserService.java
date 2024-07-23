package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(String userId, String password, String name, String email) {
        return userRepository.save(userId, password, name, email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String userId) {
        return userRepository.findById(userId);
    }
}
