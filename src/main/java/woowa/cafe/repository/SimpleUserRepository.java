package woowa.cafe.repository;

import woowa.cafe.domain.User;
import woowa.frame.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleUserRepository implements UserRepository {

    private Map<String, User> database = new ConcurrentHashMap<>();

    @Override
    public User findById(String id) {
        return database.get(id);
    }

    @Override
    public void save(User user) {
        try {
            String uuid = UUID.randomUUID().toString();
            Field id = User.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(user, uuid);
            database.put(user.getId(), user);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public void update(User user) {
        database.put(user.getId(), user);
    }
}
