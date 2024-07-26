package codesquad.jspcafe.domain.user.service;

import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.payload.request.UserLoginRequest;
import codesquad.jspcafe.domain.user.payload.request.UserUpdateRequest;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = verifyUserPassword(updateRequest.getUserId(), updateRequest.getPassword());
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

    public UserSessionResponse loginUser(UserLoginRequest loginRequest) {
        User user = verifyUserPassword(loginRequest.getUserId(), loginRequest.getPassword());
        return UserSessionResponse.from(user);
    }

    private User verifyUserPassword(String userId, String password) {
        User user = findUserById(userId);
        if (!user.verifyPassword(password)) {
            throw new SecurityException("비밀번호가 일치하지 않습니다!");
        }
        return user;
    }

    private User findUserById(String userId) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다!"));
    }

}
