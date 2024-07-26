package woowa.cafe.dto.request;

public record UpdateUserRequest(
        String userId,
        String password,
        String name,
        String email
) {
}
