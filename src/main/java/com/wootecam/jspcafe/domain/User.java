package com.wootecam.jspcafe.domain;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.util.Objects;

public class User {

    private Long id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(final String userId, final String password, final String name, final String email) {
        validate(userId, password, name, email);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this(userId, password, name, email);
        this.id = id;
    }

    private void validate(final String userId, final String password, final String name, final String email) {
        if (Objects.isNull(userId) || Objects.isNull(password) || Objects.isNull(name) || Objects.isNull(email)
                || userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new BadRequestException("회원가입 시 모든 정보를 입력해야 합니다.");
        }
    }

    public User edit(final String originalPassword, final String newPassword, final String name,
                     final String email) {
        validateUpdateInfo(originalPassword, newPassword, name, email);
        validatePassword(originalPassword);

        return new User(id, userId, newPassword, name, email);
    }

    private void validateUpdateInfo(final String originalPassword, final String newPassword, final String name,
                                    final String email) {
        if (Objects.isNull(originalPassword) || Objects.isNull(newPassword) || Objects.isNull(email) || Objects.isNull(
                name)
                || originalPassword.isEmpty() || newPassword.isEmpty() || email.isEmpty() || name.isEmpty()) {
            throw new BadRequestException("회원 수정 시 모든 정보를 입력해야 합니다.");
        }
    }

    private void validatePassword(final String originalPassword) {
        if (!originalPassword.equals(password)) {
            throw new BadRequestException("입력한 기존 비밀번호와 실제 비밀번호가 다릅니다.");
        }
    }

    public boolean confirmPassword(final String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
