package woowa.camp.jspcafe.domain;

import java.time.LocalDate;
import java.util.Objects;
import woowa.camp.jspcafe.domain.exception.UserException;

public class User {

    private Long id;
    private final String email;
    private final String nickname;
    private final String password;
    private final LocalDate registerAt;

    public User(String email, String nickname, String password, LocalDate registerAt) {
        validate(email, nickname, password);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.registerAt = registerAt;
    }

    private void validate(String password, String name, String email) {
        if (password == null || password.isEmpty()
                || name == null || name.isEmpty()
                || email == null || email.isEmpty()) {
            throw new UserException(
                    "비밀번호, 닉네임, 이메일은 값이 존재해야 합니다. {%s, %s, %s}".formatted(password, name, email));
        }
    }

    /**
     * only allowed in repository
     */
    public void setId(Long id) {
        this.id = id;
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
        return Objects.equals(id, user.id) && Objects.equals(email, user.email)
                && Objects.equals(nickname, user.nickname) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nickname, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getRegisterAt() {
        return registerAt;
    }
}
