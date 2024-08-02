package woowa.camp.jspcafe.service.dto.request;

public record ArticleWriteRequest(Long authorId, String title, String content) {
}
