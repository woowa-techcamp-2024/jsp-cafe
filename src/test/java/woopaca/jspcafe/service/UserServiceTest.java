package woopaca.jspcafe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import woopaca.jspcafe.fixture.TestRepositoryFixture;
import woopaca.jspcafe.servlet.dto.response.MembersResponse;
import woopaca.jspcafe.servlet.dto.request.SignUpRequest;
import woopaca.jspcafe.servlet.dto.response.UserProfile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(TestRepositoryFixture.testUserRepository());
    }

    @Nested
    class sign_up_메서드는 {

        @Nested
        class 유효한_회원가입_요청 {

            @Test
            void 정상적으로_회원가입에_성공한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "new", "new");
                userService.signUp(signUpRequest);
                assertThatNoException().isThrownBy(() -> userService.signUp(signUpRequest));
            }
        }

        @Nested
        class 유효하지_않은_회원가입_요청 {

            @Test
            void 이메일이_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("", "new", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이메일은 비어있을 수 없습니다.");
            }

            @Test
            void 닉네임이_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 닉네임은 비어있을 수 없습니다.");
            }

            @Test
            void 비밀번호가_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "new", "");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 비밀번호는 비어있을 수 없습니다.");
            }

            @Test
            void 이미_사용중인_이메일이면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("test@email.com", "new", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이미 사용 중인 이메일입니다.");
            }

            @Test
            void 이미_사용중인_닉네임이면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "test", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이미 사용 중인 닉네임입니다.");
            }
        }
    }

    @Nested
    class get_all_members_메서드는 {

        @Test
        void 모든_회원을_조회한다() {
            List<MembersResponse> members = userService.getAllMembers();
            assertThat(members).isNotNull();
            assertThat(members).hasSize(3);
        }
    }

    @Nested
    class get_user_profile_메서드는 {

        @Test
        void 사용자_프로필을_조회한다() {
            String userId = "111";
            UserProfile userProfile = userService.getUserProfile(userId);
            assertThat(userProfile).isNotNull();
            assertThat(userProfile.nickname()).isEqualTo("test");
        }

        @Test
        void 사용자_프로필을_조회할_수_없는_경우_예외가_발생한다() {
            String userId = "-1";
            assertThatThrownBy(() -> userService.getUserProfile(userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 사용자를 찾을 수 없습니다.");
        }
    }
}