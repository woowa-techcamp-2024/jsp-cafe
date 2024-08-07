package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record PostDetailsResponse(Long id, String title, String content, String writer, String writtenAt,
                                  int viewCount) {

    public static PostDetailsResponse of(Post post, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new PostDetailsResponse(post.getId(), post.getTitle(), post.getContent().trim(), user.getNickname(),
                post.getWrittenAt().format(formatter), post.getViewCount());
    }
}
