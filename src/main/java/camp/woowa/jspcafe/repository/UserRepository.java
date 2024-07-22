package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

public interface UserRepository {
    void save(String userId, String password, String name, String email);

    User findById(String userId);
}
