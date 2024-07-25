package codesqaud.app.model;

import codesqaud.app.exception.HttpException;

import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class User {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[\\w]+(\\.+)[\\w]+$");

    private Long id;
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        validateUserId(userId);
        validatePassword(password);
        validateName(name);
        validateEmail(email);

        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "User ID는 null이거나 비어있을 수 없습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "비밀번호는 null이거나 비어있을 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "이름은 null이거나 비어있을 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new HttpException(SC_BAD_REQUEST, "이메일은 null이거나 비어있을 수 없습니다.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new HttpException(SC_BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }
}
