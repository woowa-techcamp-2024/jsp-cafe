package woowa.cafe.dto;

public record UpdateQuestionRequest(String id, String userId, String title, String content) {
}
