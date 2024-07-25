package codesquad.infra;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.exception.DuplicateIdException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserDao implements UserDao {
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private static final ConcurrentMap<Long, User> users = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Long> userIdToIdMap = new ConcurrentHashMap<>();

    @Override
    public Long save(User user) throws DuplicateIdException {
        Long id = ID_GENERATOR.incrementAndGet();
        Long existUserId = userIdToIdMap.putIfAbsent(user.getUserId(), id);
        if (existUserId != null) {
            throw new DuplicateIdException("이미 가입된 아이디 입니다.");
        }
        user = new User(id, user);
        users.put(id, user);
        return id;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        Long id = userIdToIdMap.get(userId);
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public void update(User user) {
        Long id = user.getId();
        User oldUser = users.get(id);
        if(oldUser != null) {
            String oldUserId = oldUser.getUserId();
            userIdToIdMap.remove(oldUserId);
        }
        users.put(id, user);
        userIdToIdMap.put(user.getUserId(), id);
    }
}
