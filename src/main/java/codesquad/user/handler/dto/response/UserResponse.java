package codesquad.user.handler.dto.response;

public record UserResponse(
        Long id,
        String userId,
        String name,
        String email
) {
}
