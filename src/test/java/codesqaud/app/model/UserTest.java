package codesqaud.app.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
    private String userId = "semin";
    private String password = "password";
    private String name = "semin";
    private String email = "semin@gmail.com";

    @Nested
    class User_인스턴스_생성_시 {
        @Test
        void userId_필드가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(null, password, name, email))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new User("", password, name, email))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void password_필드가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, null, name, email))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new User(userId, "", name, email))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void name_필드가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, null, email))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new User(userId, password, "", email))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void email_필드가_비어있으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(userId, password, name, null))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new User(userId, password, name, ""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void email_형식에_맞지_않으면_예외가_발생한다() {
            assertThatThrownBy(() -> new User(null, password, name, "name"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> new User("", password, name, "semin@naver"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
