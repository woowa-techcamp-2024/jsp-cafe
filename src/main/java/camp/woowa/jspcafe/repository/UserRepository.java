package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.models.User;

import java.util.List;

public interface UserRepository {
    String save(String userId, String password, String name, String email);

    User findById(String userId);

    List<User> findAll();
}
