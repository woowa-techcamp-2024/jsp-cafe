package woowa.cafe.repository;

import woowa.cafe.domain.User;
import woowa.frame.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface UserRepository {

    User findById(String id);

    void save(User user);

    List<User> findAll();

    void update(User user);
}
