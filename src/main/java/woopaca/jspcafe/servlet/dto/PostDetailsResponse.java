package woopaca.jspcafe.servlet.dto;

import woopaca.jspcafe.model.Post;

import java.time.format.DateTimeFormatter;

public record PostDetailsResponse(String title, String content, String writer, String writtenAt, int viewCount) {

    public static PostDetailsResponse from(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new PostDetailsResponse(post.getTitle(), post.getContent().trim(), post.getWriter(), post.getWrittenAt().format(formatter), post.getViewCount());
    }
}
