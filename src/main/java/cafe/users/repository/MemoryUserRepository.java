package cafe.users.repository;

import cafe.users.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryUserRepository implements UserRepository {
    private static final List<User> users = new CopyOnWriteArrayList<>();

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            users.set(Math.toIntExact(user.getId() - 1), user);
        } else {
            user = user.withId((long) (users.size() + 1));
            users.add(user);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users);
    }

    @Override
    public User findById(Long id) {
        return users.get(Math.toIntExact(id) - 1);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
