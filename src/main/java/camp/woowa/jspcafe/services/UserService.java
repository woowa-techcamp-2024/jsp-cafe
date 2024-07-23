package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(String userId, String password, String name, String email) {
        return userRepository.save(userId, password, name, email);
    }
}
