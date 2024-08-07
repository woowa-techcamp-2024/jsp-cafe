package org.example.service;

import java.util.List;
import java.util.Optional;
import org.example.dto.UserCreateReqDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryDBImpl;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

public class UserService {

    private static final Logger logger = LoggerUtil.getLogger();
    private final UserRepository userRepository = UserRepositoryDBImpl.getInstance();

    public void createUser(
        UserCreateReqDto userCreateReqDto
    ) {

        userRepository.save(userCreateReqDto.toEntity());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    public boolean login(
        String userId,
        String password
    ) {
        Optional<User> user = userRepository.getUserByUserId(userId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public void updateUser(String userId, String password, String nickname, String email) {
        User user = userRepository.getUserByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.updateUser(userId, nickname, email);
    }
}
