package com.codesquad.cafe.db.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    public User() {
    }

    public User(Long id, String username, String password, String name, String email,
                LocalDateTime createdAt, LocalDateTime updatedAt, boolean deleted) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 필수 값입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password 필수 값입니다.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name 필수 값입니다.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email 필수 값입니다.");
        }
        if (createdAt == null || LocalDateTime.now().isBefore(createdAt)) {
            throw new IllegalArgumentException("createdAt 는 현재시간 이전이야 합니다.");
        }
        if (updatedAt == null || updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("updatedAt 은 createdAt 이후여야 합니다.");
        }
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    public static User of(String username, String password, String name, String email) {
        LocalDateTime now = LocalDateTime.now();
        return new User(null, username, password, name, email, now, now, false);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void update(String password, String name, String email) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password 필수 값입니다.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name 필수 값입니다.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email 필수 값입니다.");
        }
        this.password = password;
        this.name = name;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }

}
