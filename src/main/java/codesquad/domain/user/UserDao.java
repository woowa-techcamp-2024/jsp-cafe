package codesquad.domain.user;

import codesquad.exception.DuplicateIdException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Long save(User user) throws DuplicateIdException;

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    List<User> findAll();

    void update(User user);
}
