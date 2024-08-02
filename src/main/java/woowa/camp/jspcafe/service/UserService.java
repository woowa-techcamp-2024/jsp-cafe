package woowa.camp.jspcafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.dto.request.UserUpdateRequest;
import woowa.camp.jspcafe.repository.dto.response.UserResponse;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.request.RegistrationRequest;

public class UserService {

    private final UserRepository userRepository;
    private final DateTimeProvider dateTimeProvider;

    public UserService(UserRepository userRepository, DateTimeProvider dateTimeProvider) {
        this.userRepository = userRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public User registration(final RegistrationRequest registrationRequest) {
        Optional<User> userByEmail = userRepository.findByEmail(registrationRequest.email());
        if (userByEmail.isPresent()) {
            throw new UserException("이미 가입된 이메일입니다.");
        }

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

    public void updateUserProfile(Long userId, String password, UserUpdateRequest userUpdateRequest) {
        User user = findById(userId);

        if (!user.getPassword().equals(password)) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.update(user, userUpdateRequest);
    }

    public User login(String email, String password) {
        User user = findByEmail(email);
        if (user.isCorrectUser(email, password)) {
            return user;
        }
        throw new UserException("이메일과 비밀번호가 일치하지 않습니다");
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("존재하지 않는 이메일입니다"));
    }

}
