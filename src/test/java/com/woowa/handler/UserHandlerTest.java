package com.woowa.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.woowa.database.UserDatabase;
import com.woowa.database.UserMemoryDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.model.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserHandlerTest {

    private UserHandler userHandler;
    private UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserMemoryDatabase();
        userHandler = new UserHandler(userDatabase);
    }

    @Nested
    @DisplayName("createUser 호출 시")
    class CreateUserTest {

        @Test
        @DisplayName("새로운 사용자를 생성한다.")
        void createNewUser() {
            //given
            String email = "test@test.com";
            String nickname = "tester";
            String password = "123456";

            //when
            ResponseEntity response = userHandler.createUser(email, nickname, password);

            //then
            Optional<User> optionalUser = userDatabase.findByEmail(email);
            assertThat(optionalUser).isPresent().get()
                    .satisfies(user -> {
                        assertThat(user.getEmail()).isEqualTo(email);
                        assertThat(user.getNickname()).isEqualTo(nickname);
                        assertThat(user.getPassword()).isEqualTo(password);
                    });
        }

        @Test
        @DisplayName("회원 목록 화면으로 리다이렉트한다.")
        void redirectToUsers() {
            //given
            String email = "test@test.com";
            String nickname = "tester";
            String password = "123456";

            //when
            ResponseEntity response = userHandler.createUser(email, nickname, password);

            //then
            assertThat(response.getLocation()).isEqualTo("/users");
            assertThat(response.getStatus()).isEqualTo(302);
        }

        @Test
        @DisplayName("예외(IllegalArgument): 동일한 이메일을 가진 유저가 있으면")
        void illegalArgument_WhenDuplicateEmail() {
            //given
            String email = "test@test.com";
            String nickname = "tester";
            String password = "123456";

            userDatabase.save(User.create(UUID.randomUUID().toString(), email, password, nickname));

            //when
            Exception exception = catchException(() -> userHandler.createUser(email, nickname, password));

            //then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
