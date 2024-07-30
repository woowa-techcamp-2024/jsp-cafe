package codesqaud.app.dao.user;

import codesqaud.app.dao.CommonDao;
import codesqaud.app.model.User;

import java.util.Optional;

public interface UserDao extends CommonDao<User, Long> {
    Optional<User> findByUserId(String username);
}
