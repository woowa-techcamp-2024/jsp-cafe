package org.example.jspcafe.user.service;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.H2DatabaseConnectionManager;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.response.UserListResponse;
import org.example.jspcafe.user.response.UserProfileResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserServiceTest extends AbstractRepositoryTestSupport {

    private JdbcUserRepository userRepository = new JdbcUserRepository(super.connectionManager);
    private UserService userService = new UserService(userRepository);

    @Override
    protected void deleteAllInBatch() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 리스트를 가져올 수 있다.")
     @Test
     void getUsers() {
         // given
         User user1 = new User("nickname1", "email1@", "password1", LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
         User user2 = new User("nickname2", "email2@", "password2", LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));

         userRepository.save(user1);
         userRepository.save(user2);

         // when
         UserListResponse response = userService.getUserList();

         // then
         assertAll(
                 () -> assertThat(response.count())
                         .isEqualTo(2),
                 () -> assertThat(response.userList())
                         .extracting("nickname", "email")
                         .containsExactlyInAnyOrder(
                                 tuple("nickname1", "email1@"),
                                 tuple("nickname2", "email2@")
                         )
         );
     }

     @DisplayName("사용자의 프로필을 가져올 수 있다.")
     @Test
     void getUserProfile() {
         // given
         User user = new User("nickname", "email@", "password", LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0));
         userRepository.save(user);

         // when
         UserProfileResponse profile = userService.getProfile(user.getNickname().getValue());

         // then
         assertThat(profile)
                .extracting("nickname", "email", "createdAt")
                .containsExactly(user.getNickname().getValue(), user.getEmail().getValue(), user.getCreatedAt());
     }

     @DisplayName("없는 사용자의 프로필을 가져오려고 하면 예외가 발생한다.")
     @Test
     void getUserProfileWithNotExistsUser() {
         // given
         String nickname = "nickname";


         // when & then
         assertThatThrownBy(() -> userService.getProfile(nickname))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("사용자를 찾을 수 없습니다.");
     }

     @DisplayName("사용자의 닉네임을 수정할 수 있다.")
     @Test
     void editProfile() {
         // given
         String nickname = "nickname";
         String email = "email@example.com";
         String password = "password";

         User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

         User savedUser = userRepository.save(user);

         String newNickname = "newNickname";

         // when
         userService.editProfile(savedUser.getUserId(), newNickname, password);

         // then
         Optional<User> maybeUser = userRepository.findById(savedUser.getUserId());
         assertThat(maybeUser).isPresent()
                 .get()
                 .extracting(u -> u.getNickname().getValue())
                 .isEqualTo(newNickname);

     }

     @DisplayName("사용자의 닉네임을 수정할 때, 잘못된 userId를 입력하면 예외가 발생한다.")
     @Test
     void editProfileWithNotExistsUser() {
         // given
         String nickname = "nickname";
         String email = "email@example.com";
         String password = "password";

         User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));
         userRepository.save(user);

         String newNickname = "newNickname";
         Long invalidUserId = Long.MAX_VALUE;

         // when & then
         assertThatThrownBy(() -> userService.editProfile(invalidUserId, newNickname, password))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("사용자를 찾을 수 없습니다.");
     }

     @DisplayName("사용자의 닉네임을 수정할 때, 비밀번호가 일치하지 않으면 예외가 발생한다.")
     @Test
     void editProfileWithNotMatchPassword() {
         // given
         String nickname = "nickname";
         String email = "email@example.com";
         String password = "password";

         User user = new User(nickname, email, password, LocalDateTime.of(2021, 1, 1, 0, 0));

         User savedUser = userRepository.save(user);

         String newNickname = "newNickname";
         String invalidPassword= "invalidPassword";

         // when & then
         assertThatThrownBy(() -> userService.editProfile(savedUser.getUserId(), newNickname, invalidPassword))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("비밀번호가 일치하지 않습니다.");
     }
}