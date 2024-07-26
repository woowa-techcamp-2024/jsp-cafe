package codesquad.servlet;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.exception.DuplicateIdException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MockUserDao implements UserDao {
    private User user;

    @Override
    public Long save(User user) throws DuplicateIdException {
        user = new User(1L, user);
        return 1L;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        if (user == null) {
            return Collections.emptyList();
        }
        return List.of(user);
    }

    @Override
    public void update(User user) {

    }
}
