package codesquad.jspcafe.domain.user.service;

import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.payload.request.UserUpdateRequest;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.repository.UserMemoryRepository;
import java.util.List;
import java.util.Map;

public class UserService {

    private final UserMemoryRepository userRepository;

    public UserService() {
        userRepository = new UserMemoryRepository();
    }

    public UserCommonResponse createUser(Map<String, String[]> parameterMap) {
        String userId = parameterMap.get("userId")[0];
        String password = parameterMap.get("password")[0];
        String name = parameterMap.get("name")[0];
        String email = parameterMap.get("email")[0];
        User user = new User(userId, password, name, email);
        return UserCommonResponse.from(userRepository.save(user));
    }

    public UserCommonResponse updateUserInfo(UserUpdateRequest updateRequest) {
        verifyUserPassword(updateRequest.getUserId(), updateRequest.getPassword());
        User user = findUserById(updateRequest.getUserId());
        user.updateValues(updateRequest.getUsername(), updateRequest.getEmail());
        return UserCommonResponse.from(userRepository.update(user));
    }

    public UserCommonResponse getUserById(String userId) {
        User user = findUserById(userId);
        return UserCommonResponse.from(user);
    }

    public List<UserCommonResponse> findAllUser() {
        return userRepository.findAll().stream().map(UserCommonResponse::from).toList();
    }

    private void verifyUserPassword(String userId, String password) {
        User user = findUserById(userId);
        if (!user.verifyPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!");
        }
    }

    private User findUserById(String userId) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다!"));
    }

}
