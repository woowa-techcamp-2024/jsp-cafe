package org.example.jspcafe.user.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.response.UserListResponse;
import org.example.jspcafe.user.response.UserProfileResponse;
import org.example.jspcafe.user.response.UserResponse;

import java.util.List;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserListResponse getUserList() {
        List<UserResponse> userList = userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getNickname().getValue(), user.getEmail().getValue()))
                .toList();
        return UserListResponse.of(userList);
    }

    public UserService(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfile(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(user -> new UserProfileResponse(user.getNickname().getValue(), user.getEmail().getValue(), user.getCreatedAt()))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public void editProfile(Long userId, String newNickname, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(!user.getPassword().getValue().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        user.updateNickname(newNickname);

        userRepository.update(user);
    }
}
