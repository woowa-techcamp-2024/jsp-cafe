package woowa.camp.jspcafe.service.dto;

public record ReplyWriteRequest(Long userId, Long articleId, String content) {
}
