package woowa.cafe.dto.request;

public record CreateQuestionRequest(
        String authorName,
        String title,
        String content,
        String userId
) {
}
