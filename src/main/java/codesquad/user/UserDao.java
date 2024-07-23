package codesquad.user;

import codesquad.exception.DuplicateIdException;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user) throws DuplicateIdException;

    Optional<User> findById(String userId);

    List<User> findAll();
}
