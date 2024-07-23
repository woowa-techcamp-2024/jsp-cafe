package woowa.camp.jspcafe.domain;

import java.util.Objects;
import woowa.camp.jspcafe.domain.exception.UserException;

public class User {

    private Long id;
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        validate(userId, password, name, email);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validate(String userId, String password, String name, String email) {
        if (userId == null || userId.isEmpty()
                || password == null || password.isEmpty()
                || name == null || name.isEmpty()
                || email == null || email.isEmpty())
        {
            throw new UserException(
                    "아이디, 비밀번호, 이름, 이메일은 값이 존재해야 합니다. {%s, %s, %s, %s}".formatted(userId, password, name, email));
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
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId)
                && Objects.equals(password, user.password) && Objects.equals(name, user.name)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
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
}
