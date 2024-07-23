package woowa.camp.jspcafe.service;

import java.util.List;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;

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

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
