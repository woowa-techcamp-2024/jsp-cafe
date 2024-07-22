package camp.woowa.jspcafe.services;

import camp.woowa.jspcafe.User;

public class UserService {
    public User createUser(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }
}
