package cafe.service;

import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.dto.UserDto;

import java.util.Map;

public class SessionService {
    private final UserDatabase userDatabase;
    private final SessionDatabase sessionDatabase;

    public SessionService(UserDatabase userDatabase, SessionDatabase sessionDatabase) {
        this.userDatabase = userDatabase;
        this.sessionDatabase = sessionDatabase;
    }

    public void signIn(String sessionid, String userid, String password) {
        User user = userDatabase.selectById(userid);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("로그인 실패");
        }
        sessionDatabase.insert(new UserDto(sessionid, user));
    }

    public void signOut(String id) {
        sessionDatabase.deleteById(id);
    }

    public boolean isSignIn(String sessionid) {
        return sessionDatabase.selectAll().containsKey(sessionid);
    }

    public User findUserBySession(String id) {
        User user = (User) sessionDatabase.selectById(id);
        if (user == null) throw new IllegalArgumentException("User not found!");
        return user;
    }
}
