package codesquad.global.servlet;

import codesquad.user.domain.User;
import codesquad.user.repository.UserRepository;
import codesquad.common.exception.DuplicateIdException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MockUserRepository implements UserRepository {
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
