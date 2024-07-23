package org.example.jspcafe.user.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.response.UserListResponse;
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

    public UserService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
