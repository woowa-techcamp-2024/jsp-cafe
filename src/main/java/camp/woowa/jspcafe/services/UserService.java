package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.exception.CustomException;
import camp.woowa.jspcafe.exception.HttpStatus;
import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(String userId, String password, String name, String email) {
        return userRepository.save(userId, password, name, email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public Long update(Long id, String password, String updatedUserId, String updatedName, String updatedEmail) {
        User targetUser = findById(id);
        if (targetUser.validatePassword(password))
            return userRepository.update(id, updatedUserId, updatedName, updatedEmail);
        else
            throw new CustomException(HttpStatus.INVALID_PASSWORD);
    }
}
