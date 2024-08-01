package codesquad.user.service;

import codesquad.common.exception.AuthenticationException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.user.domain.User;
import codesquad.user.repository.UserRepository;

import java.util.Optional;

public class SignInService {
    private final UserRepository repository;

    public SignInService(UserRepository repository) {
        this.repository = repository;
    }

    public User signIn(Command cmd) throws NoSuchElementException, AuthenticationException {
        Optional<User> findUser = repository.findByUserId(cmd.userId());
        if (findUser.isEmpty()) {
            throw new NoSuchElementException();
        }
        User user = findUser.get();
        boolean matches = user.matches(cmd.password());
        if (matches) {
            return user;
        }
        throw new AuthenticationException();
    }

    public record Command(
            String userId,
            String password
    ) {
    }
}
