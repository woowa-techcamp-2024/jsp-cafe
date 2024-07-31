package woowa.cafe.service;

import woowa.cafe.domain.User;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.repository.UserRepository;
import woowa.frame.core.annotation.Component;

@Component
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfo login(String userId, String password) {
        User user = userRepository.findByUserIdAndPassword(userId, password);
        return user == null ? null : new UserInfo(user.getId(), user.getUserId(), user.getName(), user.getEmail());
    }
}
