package com.wootecam.jspcafe.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wootecam.jspcafe.exception.BadRequestException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

    @ParameterizedTest
    @MethodSource("generateInvalidUserInfo")
    void 사용자의_정보중_하나라도_비어있거나_null이라면_예외가_발생한다(List<String> invalidUserInfo) {
        // expect
        assertThatThrownBy(() -> new User(invalidUserInfo.get(0), invalidUserInfo.get(1), invalidUserInfo.get(2),
                invalidUserInfo.get(3)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
    }

    private static Stream<Arguments> generateInvalidUserInfo() {
        return Stream.of(
                Arguments.of(List.of("", "password", "name", "email")),
                Arguments.of(List.of("userId", "", "name", "email")),
                Arguments.of(List.of("userId", "password", "", "email")),
                Arguments.of(List.of("userId", "password", "name", "")),
                Arguments.of(Arrays.asList(null, "password", "name", "email")),
                Arguments.of(Arrays.asList("userId", null, "name", "email")),
                Arguments.of(Arrays.asList("userId", "password", null, "email")),
                Arguments.of(Arrays.asList("userId", "password", "name", null))
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidEditUserInfo")
    void 수정할_사용자의_정보중_하나라도_비어있거나_null이_입력되면_예외가_발생한다(List<String> invalidEditUserInfo) {
        // given
        User user = new User(1L, "userId", "password", "name", "email");

        // expect
        assertThatThrownBy(
                () -> user.edit(invalidEditUserInfo.get(0), invalidEditUserInfo.get(1), invalidEditUserInfo.get(2),
                        invalidEditUserInfo.get(3)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("회원 수정 시 모든 정보를 입력해야 합니다.");
    }

    private static Stream<Arguments> generateInvalidEditUserInfo() {
        return Stream.of(
                Arguments.of(List.of("", "newPassword", "name", "email")),
                Arguments.of(List.of("originalPassword", "", "name", "email")),
                Arguments.of(List.of("originalPassword", "newPassword", "", "email")),
                Arguments.of(List.of("originalPassword", "newPassword", "name", "")),
                Arguments.of(Arrays.asList(null, "newPassword", "name", "email")),
                Arguments.of(Arrays.asList("originalPassword", null, "name", "email")),
                Arguments.of(Arrays.asList("originalPassword", "newPassword", null, "email")),
                Arguments.of(Arrays.asList("originalPassword", "newPassword", "name", null))
        );
    }

    @Test
    void 회원_수정_시_기존_비밀번호가_다르면_예외가_발생한다() {
        // given
        User user = new User(1L, "userId", "password", "name", "email");

        // expect
        assertThatThrownBy(
                () -> user.edit("differentPassword", "newPassword", "name", "email"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("입력한 기존 비밀번호와 실제 비밀번호가 다릅니다.");
    }

    @Test
    void 회원_정보를_수정할_수_있다() {
        // given
        User user = new User(1L, "userId", "password", "name", "email");

        // when
        User editedUser = user.edit("password", "newPassword", "newName", "newEmail");

        // then
        assertAll(
                () -> assertThat(editedUser.getId()).isEqualTo(1L),
                () -> assertThat(editedUser.getUserId()).isEqualTo("userId"),
                () -> assertThat(editedUser.getPassword()).isEqualTo("newPassword"),
                () -> assertThat(editedUser.getName()).isEqualTo("newName"),
                () -> assertThat(editedUser.getEmail()).isEqualTo("newEmail")
        );
    }
}
