package org.example.jspcafe.user.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserListResponseTest {

    @DisplayName("UserListResponse 생성")
    @Test
    void create() {
        // given
        List<UserResponse> userList = List.of(
                new UserResponse("nickname1", "email1"),
                new UserResponse("nickname2", "email2")
        );

        // when
        UserListResponse userListResponse = UserListResponse.of(userList);

        // then
        assertThat(userListResponse)
                .extracting("count", "userList")
                .containsExactly(userList.size(), userList);
    }

}