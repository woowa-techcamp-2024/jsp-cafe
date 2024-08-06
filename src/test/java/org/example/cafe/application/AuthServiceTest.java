package org.example.cafe.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.cafe.application.dto.LoginDto;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infra.UserJdbcRepository;
import org.example.cafe.infra.database.DbConnector;
import org.example.cafe.infra.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("AuthService 테스트")
class AuthServiceTest {

    DbConnector dbConnector = new DbConnector().init();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnector.getDataSource());
    UserRepository userRepository = new UserJdbcRepository(jdbcTemplate);
    AuthService authService = new AuthService(userRepository);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    class 사용자를_인증한다 {

        @Nested
        class 성공 {
            @Test
            void 올바른_사용자명과_비밀번호를_입력하면_인증할_수_있다() {
                // Given
                User user = new User("validUser", "correctPassword", "nickname", "email@example.com");
                userRepository.save(user);
                LoginDto loginDto = new LoginDto("validUser", "correctPassword");

                // When
                boolean result = authService.authenticate(loginDto);

                // Then
                assertTrue(result);
            }
        }

        @Nested
        class 실패 {
            @Test
            void 실패_올바른_사용자명과_틀린_비밀번호를_입력하면_인증할_수_없다() {
                // Given
                User user = new User("validUser", "correctPassword", "nickname", "email@example.com");
                userRepository.save(user);
                LoginDto loginDto = new LoginDto("validUser", "wrongPassword");

                // When
                boolean result = authService.authenticate(loginDto);

                // Then
                assertFalse(result);
            }

            @Test
            void 실패_없는_사용자명과_비밀번호를_입력하면_인증할_수_없다() {
                // Given
                LoginDto loginDto = new LoginDto("validUser", "password");

                // When
                boolean result = authService.authenticate(loginDto);

                // Then
                assertFalse(result);
            }
        }
    }
}