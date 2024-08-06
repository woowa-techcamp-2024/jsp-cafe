package cafe.domain.entity;

import java.time.LocalDateTime;

public class User {
    private final String userId;
    private final String name;
    private final String password;
    private final String email;
    private final String created;

    public static User of(String id, String name, String password, String email) {
        return new User(id, name, password, email, LocalDateTime.now().toString());
    }

    private User(String id, String name, String password, String email, String created) {
        validate(id, password, name, email);
        this.userId = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.created = created;
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

    public String getCreated() {
        return created;
    }

    public void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id is empty!");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is empty!");
        }
    }

    public void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name is empty!");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is empty!");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email is invalid!");
        }
    }

    public void validate(String id, String password, String name, String email) {
        validateId(id);
        validatePassword(password);
        validateName(name);
        validateEmail(email);
    }
}
