package woowa.cafe.service;

import woowa.cafe.domain.User;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.dto.request.CreateUserRequest;
import woowa.cafe.dto.request.UpdateUserRequest;
import woowa.cafe.repository.UserRepository;
import woowa.frame.core.annotation.Component;

import java.util.List;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserRequest request) {
        User user = new User(request.userId(), request.password(), request.name(), request.email());
        userRepository.save(user);
    }

    public List<UserInfo> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserInfo(user.getId(), user.getUserId(), user.getName(), user.getEmail()))
                .toList();
    }

    public UserInfo getProfile(String userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return null;
        }

        return new UserInfo(user.getId(), user.getUserId(), user.getName(), user.getEmail());
    }

    public void updateUser(UpdateUserRequest req) {
        User user = userRepository.findById(req.userId());

        if(!req.password().equals(user.getPassword())) {
            throw new RuntimeException("Password is not correct");
        }

        if(req.name() != null) {
            user.setName(req.name());
        }

        if(req.email() != null) {
            user.setEmail(req.email());
        }
    }
}
