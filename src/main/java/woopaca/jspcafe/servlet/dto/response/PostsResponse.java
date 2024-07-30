package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record PostsResponse(Long id, String title, String writer, String writtenAt, int viewCount) {

    public static PostsResponse of(Post post, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return new PostsResponse(post.getId(), post.getTitle(), user.getNickname(), post.getWrittenAt().format(formatter), post.getViewCount());
    }
}
