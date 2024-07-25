package woopaca.jspcafe.servlet.dto;

import woopaca.jspcafe.model.Post;

import java.time.format.DateTimeFormatter;

public record PostDetailsResponse(String title, String content, String writer, String writtenAt, int viewCount,
                                  boolean hasNext, boolean hasPrevious, Long nextPostId, Long previousPostId) {

    public static PostDetailsResponse of(Post post, boolean hasNext, boolean hasPrevious, Long nextPostId, Long previousPostId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new PostDetailsResponse(post.getTitle(), post.getContent().trim(), post.getWriter(),
                post.getWrittenAt().format(formatter), post.getViewCount(), hasNext, hasPrevious, nextPostId, previousPostId);
    }
}
