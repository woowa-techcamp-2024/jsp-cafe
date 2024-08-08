package com.wootecam.jspcafe.service;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.domain.UserRepository;
import com.wootecam.jspcafe.exception.BadRequestException;
import com.wootecam.jspcafe.exception.NotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(final String userId, final String password, final String name, final String email) {
        User user = new User(userId, password, name, email);

        log.info("signUpUser = {}", user);

        userRepository.save(user);
    }

    public int countAll() {
        return userRepository.countAll();
    }

    public List<User> readAll(final int page, final int size) {
        return userRepository.findAllOrderByIdDesc(page, size);
    }

    public User read(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. input path = " + id));
    }

    public void edit(final Long id,
                     final String originalPassword,
                     final String newPassword,
                     final String name,
                     final String email) {
        User user = read(id);
        User editedUser = user.edit(originalPassword, newPassword, name, email);

        userRepository.update(editedUser);
    }

    public Optional<User> signIn(final String userId, final String password) {
        validateSignInInfo(userId, password);

        return userRepository.findByUserId(userId)
                .filter(user -> user.confirmPassword(password));
    }

    private void validateSignInInfo(final String userId, final String password) {
        if (Objects.isNull(userId) || Objects.isNull(password)
                || userId.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("로그인 정보를 모두 입력해야 합니다.");
        }
    }

    public User readSignInUser(final Long editId, final User signInUser) {
        if (Objects.isNull(editId) || Objects.isNull(signInUser)) {
            throw new NotFoundException("프로필 수정을 할 사용자를 찾을 수 없습니다.");
        }

        if (!editId.equals(signInUser.getId())) {
            throw new BadRequestException("자신의 프로필만 수정할 수 있습니다.");
        }

        return read(editId);
    }
}
