package cafe.users;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryUserRepository implements UserRepository {
    private static final List<User> users = new CopyOnWriteArrayList<>();

    @Override
    public User save(User user) {
        User newUser = user.withId((long) (users.size() + 1));
        users.add(newUser);
        return newUser;
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users);
    }

    @Override
    public User findById(Long id) {
        return users.get(Math.toIntExact(id) - 1);
    }
}
