package cafe.service;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;

import java.util.Map;

public class UserService {
    private final UserDatabase userDatabase;

    public UserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void save(String id, String name, String password, String email) {
        userDatabase.insert(User.of(id, name, password, email));
    }

    public User find(String uri) {
        String id = uri.split("/")[2];
        User user = userDatabase.selectById(id);
        if (user == null) throw new IllegalArgumentException("User not found!");
        return user;
    }

    public Map<String, User> findAll() {
        return userDatabase.selectAll();
    }

    public void update(String uri, String name, String password, String email, String beforePassword) {
        String id = uri.split("/")[2];
        User user = userDatabase.selectById(id);
        if (user == null) throw new IllegalArgumentException("User not found!");

        validatePassword(beforePassword, user.getPassword());
        userDatabase.update(id, User.of(user.getUserId(), name, password, email));
    }

    private void validatePassword(String before, String real) {
        if (!real.equals(before)) {
            throw new IllegalArgumentException("Password is incorrect!");
        }
    }

    public void verifyUserId(User user, String requestURI) {
        String userId = requestURI.split("/")[2];
        if (!user.getUserId().equals(userId)) {
            throw new IllegalArgumentException("다른 사용자 정보를 수정할 수 없습니다.");
        }
    }
}
