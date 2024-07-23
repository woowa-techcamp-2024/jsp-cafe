package codesquad.jspcafe.domain.user.service;

import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public User createUser(Map<String, String[]> parameterMap) {
        String userId = parameterMap.get("userId")[0];
        String password = parameterMap.get("password")[0];
        String name = parameterMap.get("name")[0];
        String email = parameterMap.get("email")[0];
        User user = new User(userId, password, name, email);
        return userRepository.save(user);
    }

    public UserCommonResponse getUserById(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        return UserCommonResponse.from(user);
    }

    public List<UserCommonResponse> findAllUser() {
        return userRepository.findAll().stream().map(UserCommonResponse::from).toList();
    }

}
