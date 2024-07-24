package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("사용자 생성 테스트")
    @Test
    void createUser() {
        // given
        String nickname = "nickname";
        String email = "example@email.com";
        String password = "password";

        // when
        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        // then
        assertThat(user).isNotNull()
                .extracting(u -> u.getUserId(), u -> u.getNickname().getValue(), u -> u.getEmail().getValue(), u -> u.getPassword().getValue())
                .containsExactly(null, nickname, email, password);
    }

    @DisplayName("비밀번호 변경 테스트")
    @Test
    void updatePassword() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";

        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        String newPassword = "newPassword";

        // when
        user.updatePassword(newPassword);

        // then
        assertThat(user.getPassword().getValue()).isEqualTo(newPassword);

    }

    @DisplayName("같은 비밀번호로 변경하면 예외가 발생한다")
    @Test
    void updatePasswordSamePassword() {
        // given
        String nickname = "nickname";
        String email = "eamil@example.com";
        String password = "password";

        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when & then
        assertThatThrownBy(() -> user.updatePassword(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 비밀번호와 동일합니다.");
    }

    @DisplayName("생성일자 없이 사용자 생성 시 예외가 발생한다")
    @Test
    void createUserWithoutCreatedAt() {
        // given
        String nickname = "nickname";
        String email = "eamil@example.com";
        String password = "password";

        // when & then
        assertThatThrownBy(() -> new User(nickname, email, password, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("생성일자는 필수입니다.");
    }

    @DisplayName("닉네임 변경 테스트")
    @Test
    void updateNickname() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";

        User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

        // when
        user.updateNickname("newNickname");

        // then
        assertThat(user.getNickname().getValue())
                .isEqualTo("newNickname");
    }
}