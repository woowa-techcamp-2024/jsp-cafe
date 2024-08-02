package woowa.camp.jspcafe.service.dto.request;

public record ReplyWriteRequest(Long userId, Long articleId, String content) {
}
