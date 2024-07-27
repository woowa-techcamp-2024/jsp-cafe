package woowa.cafe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.cafe.domain.User;
import woowa.cafe.dto.request.UpdateUserRequest;
import woowa.cafe.repository.SimpleUserRepository;
import woowa.cafe.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private final UserService userService;
    private final UserRepository userRepository = new SimpleUserRepository();

    {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("비밀번호가 다른 경우 유저를 정보를 수정할 수 없다.")
    public void updateUserWithDifferentPassword() {
        // given
        User user = new User(
                "test",
                "password",
                "name",
                "email"
        );
        userRepository.save(user);

        String newPassword = "newPassword";
        String newName = "newName";
        String newEmail = "newEmail";
        UpdateUserRequest req = new UpdateUserRequest(user.getId(), newPassword, newName, newEmail);

        // when & then
        assertThatThrownBy(() -> {
            userService.updateUser(req);
        }).hasMessage("Password is not correct");
    }
}