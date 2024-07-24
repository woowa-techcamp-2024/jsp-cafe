package woowa.cafe.repository;

import woowa.cafe.domain.User;
import woowa.frame.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepository {

    private Map<String, User> database = new ConcurrentHashMap<>();

    public User findById(String id) {
        return database.get(id);
    }

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

    public List<User> findAll() {
        return database.values().stream().toList();
    }
}
