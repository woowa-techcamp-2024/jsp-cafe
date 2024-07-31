package repository.users;

import domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    List<User> findAll();

    void updateUser(User user);

}
