package org.example.jspcafe.user.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;

@Component
public class SignupService {

    private final UserRepository userRepository;

    public void registerUser(final RegisterUserServiceRequest request) {
        final String nickname = request.nickname();
        final String email = request.email();
        final String password = request.password();

        User user = new User(nickname, email, password);

        userRepository.save(user);
    }


    public SignupService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
