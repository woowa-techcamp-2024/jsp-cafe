package woopaca.jspcafe.servlet.dto;

import woopaca.jspcafe.model.Post;

import java.time.format.DateTimeFormatter;

public record PostsResponse(String title, String writer, String writtenAt, int viewCount) {

    public static PostsResponse from(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return new PostsResponse(post.getTitle(), post.getWriter(), post.getWrittenAt().format(formatter), post.getViewCount());
    }
}
