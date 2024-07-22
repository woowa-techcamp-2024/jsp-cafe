package org.example.jspcafe.user.service;

import org.example.jspcafe.Repository;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;

public class SignupService {

    private final Repository<User> userRepository;

    public void registerUser(final RegisterUserServiceRequest request) {
        final String nickname = request.nickname();
        final String email = request.email();
        final String password = request.password();

        User user = new User(nickname, email, password);

        userRepository.save(user);
        System.out.println("user = " + user.getNickname() + " " + user.getEmail());

    }


    public SignupService(Repository<User> userRepository) {
        this.userRepository = userRepository;
    }
}
