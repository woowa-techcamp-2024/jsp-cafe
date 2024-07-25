package woowa.camp.jspcafe.service.dto;

public record ArticleWriteRequest(Long authorId, String title, String content) {
}
