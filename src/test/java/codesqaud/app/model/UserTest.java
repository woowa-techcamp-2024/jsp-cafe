package codesqaud.app.model;

import codesqaud.app.exception.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {
    private String userId = "semin";
    private String password = "password";
    private String name = "semin";
    private String email = "semin@gmail.com";

    @Nested
    class 생성자_유효성_검사 {
        @Test
        void userId가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(null, password, name, email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("User ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void userId가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User("", password, name, email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("User ID는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void password가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, null, name, email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("비밀번호는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void password가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, "", name, email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("비밀번호는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void name이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, null, email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이름은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void name이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, "", email))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이름은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, name, null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, name, ""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_유효하지_않으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, name, "invalid-email"))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일 형식이 올바르지 않습니다.");
        }

        @Test
        void 올바른_값으로_생성된_User는_정상적으로_생성된다() {
            User user = new User(userId, password, name, email);

            assertThat(user.getUserId()).isEqualTo(userId);
            assertThat(user.getPassword()).isEqualTo(password);
            assertThat(user.getName()).isEqualTo(name);
            assertThat(user.getEmail()).isEqualTo(email);
        }
    }

    @Nested
    class Setter_유효성_검사 {
        private User user;

        @BeforeEach
        void setUp() {
            user = new User(userId, password, name, email);
        }

        @Test
        void password가_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setPassword(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("비밀번호는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void password가_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setPassword(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("비밀번호는 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void name이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setName(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이름은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void name이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setName(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이름은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_null이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setEmail(null))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_빈_문자열이면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setEmail(""))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일은 null이거나 비어있을 수 없습니다.");
        }

        @Test
        void email이_유효하지_않으면_예외가_발생한다() {
            assertThatThrownBy(() -> user.setEmail("invalid-email"))
                    .isInstanceOf(HttpException.class)
                    .hasMessageContaining("이메일 형식이 올바르지 않습니다.");
        }

        @Test
        void 올바른_값으로_설정하면_정상적으로_반영된다() {
            user.setPassword("newPassword");
            user.setName("newName");
            user.setEmail("newEmail@example.com");

            assertThat(user.getPassword()).isEqualTo("newPassword");
            assertThat(user.getName()).isEqualTo("newName");
            assertThat(user.getEmail()).isEqualTo("newEmail@example.com");
        }
    }
}
