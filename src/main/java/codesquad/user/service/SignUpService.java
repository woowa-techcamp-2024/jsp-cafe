package codesquad.user.service;

import codesquad.common.exception.DuplicateIdException;
import codesquad.user.domain.User;
import codesquad.user.repository.UserRepository;

public class SignUpService {
    private final UserRepository repository;

    public SignUpService(UserRepository repository) {
        this.repository = repository;
    }

    public Long signUp(Command cmd) throws DuplicateIdException {
        return repository.save(new User(cmd.userId(), cmd.password(), cmd.name(), cmd.email()));
    }

    public record Command(
            String userId,
            String password,
            String name,
            String email
    ) {
    }
}
