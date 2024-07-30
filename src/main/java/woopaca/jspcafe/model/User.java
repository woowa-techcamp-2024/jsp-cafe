package woopaca.jspcafe.model;

import woopaca.jspcafe.error.BadRequestException;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private LocalDateTime createdAt;

    public User(Long id, String username, String nickname, String password, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(String username, String nickname, String password) {
        this(null, username, nickname, password, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean matchPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public void updateNickname(String nickname) {
        if (nickname.isBlank()) {
            throw new BadRequestException("[ERROR] 닉네임은 비어있을 수 없습니다.");
        }
        this.nickname = nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
