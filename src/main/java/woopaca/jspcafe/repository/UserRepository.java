package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.User;

import java.util.List;

public interface UserRepository {

    void save(User user);

    List<User> findAll();
}
