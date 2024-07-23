package woopaca.jspcafe.service;

import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.UserMemoryRepository;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.MembersResponse;
import woopaca.jspcafe.servlet.dto.SignUpRequest;

import java.util.List;

public class UserService {

    private final UserRepository userRepository = new UserMemoryRepository();

    public void signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);
        User user = new User(signUpRequest.username(), signUpRequest.nickname(), signUpRequest.password());
        userRepository.save(user);
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        if (signUpRequest.username().isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일은 비어있을 수 없습니다.");
        }
        if (signUpRequest.nickname().length() < 2) {
            throw new IllegalArgumentException("[ERROR] 닉네임은 비어있을 수 없습니다.");
        }
        if (signUpRequest.password().length() < 4) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 비어있을 수 없습니다.");
        }
    }

    public List<MembersResponse> getAllMembers() {
        return userRepository.findAll()
                .stream()
                .map(MembersResponse::from)
                .toList();
    }
}
