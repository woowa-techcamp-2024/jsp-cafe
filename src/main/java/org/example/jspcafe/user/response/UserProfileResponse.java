package org.example.jspcafe.user.response;

import java.time.LocalDateTime;

public record UserProfileResponse(
        String nickname,
        String email,
        LocalDateTime createdAt
) {

}
