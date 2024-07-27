package woopaca.jspcafe.servlet.dto.response;

public record PageInfo(boolean hasNext, boolean hasPrevious, Long nextPostId, Long previousPostId) {
}
