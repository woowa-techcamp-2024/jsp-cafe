package woopaca.jspcafe.service;

import woopaca.jspcafe.model.Authentication;
import woopaca.jspcafe.model.User;
import woopaca.jspcafe.repository.UserRepository;
import woopaca.jspcafe.servlet.dto.request.SignUpRequest;
import woopaca.jspcafe.servlet.dto.request.UpdateProfileRequest;
import woopaca.jspcafe.servlet.dto.response.MembersResponse;
import woopaca.jspcafe.servlet.dto.response.UserProfile;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);
        validateDuplicateUsername(signUpRequest.username());
        validateDuplicateNickname(signUpRequest.nickname());
        User user = new User(signUpRequest.username(), signUpRequest.nickname(), signUpRequest.password());
        userRepository.save(user);
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        if (signUpRequest.username().isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이메일은 비어있을 수 없습니다.");
        }
        if (signUpRequest.nickname().isBlank()) {
            throw new IllegalArgumentException("[ERROR] 닉네임은 비어있을 수 없습니다.");
        }
        if (signUpRequest.password().isBlank()) {
            throw new IllegalArgumentException("[ERROR] 비밀번호는 비어있을 수 없습니다.");
        }
    }

    public List<MembersResponse> getAllMembers() {
        return userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getCreatedAt).reversed())
                .map(MembersResponse::from)
                .toList();
    }

    public UserProfile getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 사용자를 찾을 수 없습니다."));
        return UserProfile.from(user);
    }

    public void updateUserProfile(Long userId, UpdateProfileRequest updateProfileRequest, Authentication authentication) {
        if (!authentication.isPrincipal(userId)) {
            throw new IllegalArgumentException("[ERROR] 다른 사용자의 프로필을 수정할 수 없습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 사용자를 찾을 수 없습니다."));
        if (!user.matchPassword(updateProfileRequest.password())) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }

        String newNickname = updateProfileRequest.nickname().trim();
        if (Objects.equals(user.getNickname(), newNickname)) {
            return;
        }

        validateDuplicateNickname(newNickname);
        user.updateNickname(newNickname);
        userRepository.save(user);
    }

    private void validateDuplicateUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("[ERROR] 이미 사용 중인 이메일입니다.");
                });
    }

    private void validateDuplicateNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("[ERROR] 이미 사용 중인 닉네임입니다.");
                });
    }
}
