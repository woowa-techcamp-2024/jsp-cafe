package codesquad.user;

import codesquad.exception.DuplicateIdException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryUserDao implements UserDao {
    private static final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) throws DuplicateIdException {
        User existUser = users.putIfAbsent(user.getId(), user);
        if (existUser != null) {
            throw new DuplicateIdException("이미 가입된 아이디 입니다.");
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }
}
