package codesquad.user.service;

import codesquad.common.exception.IncorrectPasswordException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.user.domain.User;
import codesquad.user.repository.UserRepository;

import java.util.Optional;

public class UpdateUserService {
    private final UserRepository repository;

    public UpdateUserService(UserRepository repository) {
        this.repository = repository;
    }

    public void update(Command cmd) throws NoSuchElementException, IncorrectPasswordException {
        Optional<User> findUser = repository.findById(cmd.id());
        if (findUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        User user = findUser.get();
        user.update(cmd.password(), cmd.name(), cmd.email());
        repository.update(user);
    }

    public record Command(
            Long id,
            String password,
            String name,
            String email
    ) {
    }
}
