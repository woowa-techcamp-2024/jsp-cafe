package repository.users;

import domain.Users;
import dto.UsersDao;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void saveUser(Users user);

    Optional<Users> findById(Long id);

    Optional<Users> findByUserId(String userId);

    List<Users> findAll();

    void updateUser(Users user);

}
