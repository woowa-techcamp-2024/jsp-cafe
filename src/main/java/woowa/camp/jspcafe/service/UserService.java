package woowa.camp.jspcafe.service;

import java.util.ArrayList;
import java.util.List;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;
import woowa.camp.jspcafe.service.dto.UserResponse;
import woowa.camp.jspcafe.utils.time.DateTimeProvider;

public class UserService {

    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public UserService(UserRepository userRepository, DateTimeProvider dateTimeProvider) {
        this.userRepository = userRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public User registration(final RegistrationRequest registrationRequest) {
        // TODO: 회원 가입 검증 기능
        User user = new User(
                registrationRequest.email(),
                registrationRequest.nickname(),
                registrationRequest.password(),
                dateTimeProvider.getNow());

        userRepository.save(user);
        return user;
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
