package org.example.jspcafe.user.service;

import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.response.UserListResponse;
import org.example.jspcafe.user.response.UserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserServiceTest {

    private UserService userService;
    private InMemoryUserRepository userRepository;

     @BeforeEach
     void setUp() {
         userRepository = new InMemoryUserRepository();
         userService = new UserService(userRepository);
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


}