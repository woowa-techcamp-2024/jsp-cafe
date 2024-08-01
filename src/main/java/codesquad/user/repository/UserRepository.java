package codesquad.user.repository;

import codesquad.common.exception.DuplicateIdException;
import codesquad.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long save(User user) throws DuplicateIdException;

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    List<User> findAll();

    void update(User user);
}
