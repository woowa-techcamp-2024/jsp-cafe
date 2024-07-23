package org.example.jspcafe.user.service;

import org.example.jspcafe.Component;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.request.LoginServiceRequest;
import org.example.jspcafe.user.request.LoginServiceResponse;

@Component
public class LoginService {

    private final UserRepository userRepository;

    public LoginServiceResponse login (final LoginServiceRequest request) {

        final String email = request.email();
        final String password = request.password();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().getValue().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginServiceResponse(user.getUserId());
    }


    public LoginService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
