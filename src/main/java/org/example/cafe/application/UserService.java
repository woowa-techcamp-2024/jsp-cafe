package org.example.cafe.application;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.example.cafe.application.dto.UserCreateDto;
import org.example.cafe.application.dto.UserUpdateDto;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.common.exception.CafeException;
import org.example.cafe.common.exception.DataNotFoundException;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserCreateDto userCreateDto) {
        String userId = userCreateDto.userId();
        if (userRepository.findById(userId) != null) {
            throw new CafeException(HttpServletResponse.SC_BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }

        User user = userCreateDto.toUser();
        userRepository.save(user);
    }

    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }

        String checkPassword = userUpdateDto.checkPassword();
        if (!user.matchPassword(checkPassword)) {
            throw new BadAuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        User updatedUser = userUpdateDto.toUser(userId);
        userRepository.update(updatedUser);

        return updatedUser;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new DataNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return user;
    }
}
