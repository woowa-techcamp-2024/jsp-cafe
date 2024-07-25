package codesquad.jspcafe.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.payload.request.UserUpdateRequest;
import codesquad.jspcafe.domain.user.payload.response.UserCommonResponse;
import codesquad.jspcafe.domain.user.repository.UserMemoryRepository;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UserService는")
class UserServiceTest {

    private UserRepository userRepository = new UserMemoryRepository();
    private final UserService userService = new UserService(userRepository);

    private final String expectedUserId = "test";
    private final String expectedPassword = "test";
    private final String expectedName = "test";
    private final String expectedEmail = "test@jspcafe.com";

    @BeforeEach
    void clear() {
        userRepository = new UserMemoryRepository();
        for (Field field : userService.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                field.set(userService, userRepository);
            } catch (IllegalAccessException ignore) {
            }
        }
    }

    @Test
    @DisplayName("유저를 생성할 수 있다.")
    void createUser() {
        // Arrange
        Map<String, String[]> expectedValues = Map.of(
            "userId", new String[]{expectedUserId},
            "password", new String[]{expectedPassword},
            "name", new String[]{expectedName},
            "email", new String[]{expectedEmail}
        );
        // Act
        UserCommonResponse actualResult = userService.createUser(expectedValues);
        // Assert
        assertThat(actualResult)
            .extracting("userId", "username", "email")
            .containsExactly(expectedUserId, expectedName, expectedEmail);
    }

    @Test
    @DisplayName("동일한 아이디의 유저는 생성시 오류를 발생시킨다.")
    void createUserFailed() {
        // Arrange
        userRepository.save(
            new User(expectedUserId, expectedPassword, expectedName, expectedEmail));
        Map<String, String[]> expectedValues = Map.of(
            "userId", new String[]{expectedUserId},
            "password", new String[]{expectedPassword},
            "name", new String[]{expectedName},
            "email", new String[]{expectedEmail}
        );
        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(expectedValues))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 존재하는 유저입니다.");
    }

    @Nested
    @DisplayName("유저를 조회할 때")
    class whenGetUser {

        @Test
        @DisplayName("유저가 존재하지 않으면 예외를 던진다.")
        void getUserByIdFailed() {
            // Act & Assert
            assertThatThrownBy(() -> userService.getUserById(expectedUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저를 찾을 수 없습니다!");
        }

        @Test
        @DisplayName("유저가 존재하면 유저를 반환한다.")
        void getUserByIdSuccess() {
            // Arrange
            userRepository.save(
                new User(expectedUserId, expectedPassword, expectedName, expectedEmail));
            // Act
            UserCommonResponse actualResult = userService.getUserById(expectedUserId);
            // Assert
            assertThat(actualResult)
                .extracting("userId", "username", "email")
                .containsExactly(expectedUserId, expectedName, expectedEmail);
        }

        @Test
        @DisplayName("모든 유저를 조회할 수 있다.")
        void findAllUser() {
            // Arrange
            User expectedUser = new User(expectedUserId, expectedPassword, expectedName,
                expectedEmail);
            userRepository.save(expectedUser);
            // Act
            List<UserCommonResponse> actualResult = userService.findAllUser();
            // Assert
            assertAll(
                () -> assertThat(actualResult).hasSize(1),
                () -> assertThat(actualResult.get(0))
                    .extracting("userId", "username", "email")
                    .containsExactly(expectedUserId, expectedName, expectedEmail)
            );

        }

    }

    @Nested
    @DisplayName("유저 정보를 수정할 때")
    class whenUpdateUserInfo {

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 예외를 던진다.")
        void updateUserInfoFailed() {
            // Arrange
            userRepository.save(
                new User(expectedUserId, expectedPassword, expectedName, expectedEmail));
            UserUpdateRequest expectedDto = UserUpdateRequest.from(Map.of(
                "userId", new String[]{expectedUserId},
                "password", new String[]{"wrongPassword"},
                "username", new String[]{expectedName},
                "email", new String[]{expectedEmail}
            ));
            // Act & Assert
            assertThatThrownBy(
                () -> userService.updateUserInfo(expectedDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다!");
        }

        @Test
        @DisplayName("유저 정보를 수정할 수 있다.")
        void updateUserInfoSuccess() {
            // Arrange
            String expectedUpdatedUsername = "updatedName";
            String expectedUpdatedEmail = "test@gmail.com";
            userRepository.save(
                new User(expectedUserId, expectedPassword, expectedName, expectedUpdatedEmail));
            UserUpdateRequest expectedDto = UserUpdateRequest.from(Map.of(
                "userId", new String[]{expectedUserId},
                "password", new String[]{expectedPassword},
                "username", new String[]{expectedUpdatedUsername},
                "email", new String[]{expectedUpdatedEmail}
            ));
            // Act
            UserCommonResponse actualResult = userService.updateUserInfo(expectedDto);
            // Assert
            assertThat(actualResult)
                .extracting("userId", "username", "email")
                .containsExactly(expectedUserId, expectedUpdatedUsername, expectedUpdatedEmail);
        }

        @Test
        @DisplayName("존재하지 않는 유저를 수정할 수 없다.")
        void updateUserInfoFailedByNotExistUser() {
            // Arrange
            UserUpdateRequest expectedDto = UserUpdateRequest.from(Map.of(
                "userId", new String[]{expectedUserId},
                "password", new String[]{expectedPassword},
                "username", new String[]{expectedName},
                "email", new String[]{expectedEmail}
            ));
            // Act & Assert
            assertThatThrownBy(
                () -> userService.updateUserInfo(expectedDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유저를 찾을 수 없습니다!");
        }
    }

}