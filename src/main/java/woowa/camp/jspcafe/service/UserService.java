package woowa.camp.jspcafe.service;

import java.util.ArrayList;
import java.util.List;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;
import woowa.camp.jspcafe.service.dto.UserResponse;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registration(final RegistrationRequest registrationRequest) {
        // TODO: 회원 가입 검증 기능
        User user = new User(registrationRequest.userId(),
                registrationRequest.password(),
                registrationRequest.name(),
                registrationRequest.email());

        userRepository.save(user);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(UserResponse.of(user));
        }

        return userResponses;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User with id " + id + " not found"));
    }

}
