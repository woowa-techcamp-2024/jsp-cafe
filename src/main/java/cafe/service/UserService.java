package cafe.service;

import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.dto.UserDto;

import java.util.Map;

public class UserService {
    private final UserDatabase userDatabase;
    private final SessionDatabase sessionDatabase;

    public UserService(UserDatabase userDatabase, SessionDatabase sessionDatabase) {
        this.userDatabase = userDatabase;
        this.sessionDatabase = sessionDatabase;
    }

    public void save(String id, String name, String password, String email) {
        userDatabase.insert(User.of(id, password, name, email));
    }

    public UserDto find(String uri) {
        String id = uri.split("/")[2];
        User user = userDatabase.selectById(id);
        if (user == null) throw new IllegalArgumentException("User not found!");
        return new UserDto(id, user);
    }

    public UserDto findBySession(String id) {
        User user = (User) sessionDatabase.selectById(id);
        Map<String, User> users = userDatabase.selectAll();

        String uuid = null;
        for (String key : users.keySet()) {
            if (users.get(key).getUserid().equals(user.getUserid())) {
                uuid = key;
                user = users.get(key);
            }
        }

        if (user == null) throw new IllegalArgumentException("User not found!");
        return new UserDto(uuid, user);
    }

    public Map<String, User> findAll() {
        return userDatabase.selectAll();
    }

    public void update(String uri, String name, String password, String email, String beforePassword) {
        String id = uri.split("/")[2];
        User user = userDatabase.selectById(id);
        if (user == null) throw new IllegalArgumentException("User not found!");

        validatePassword(beforePassword, user.getPassword());
        userDatabase.update(id, User.of(user.getUserid(), password, name, email));
    }

    private void validatePassword(String before, String real) {
        if (!real.equals(before)) {
            throw new IllegalArgumentException("Password is incorrect!");
        }
    }
}
