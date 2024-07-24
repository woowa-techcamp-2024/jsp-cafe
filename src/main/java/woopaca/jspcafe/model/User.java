package woopaca.jspcafe.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class User {

    private String id;
    private String username;
    private String nickname;
    private String password;
    private LocalDate createdAt;

    public User(String id, String username, String nickname, String password, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
    }

    public User(String username, String nickname, String password) {
        this(null, username, nickname, password, LocalDate.now());
    }

    public String getId() {
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /**
     * UUID를 사용해 id 생성
     * @return 만약 이미 id가 존재한다면 기존 id를 반환,
     *        없다면 새로운 id를 생성하고 반환
     */
    public String generateUniqueId() {
        if (id != null) {
            return id;
        }

        this.id = UUID.randomUUID()
                .toString();
        return id;
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
