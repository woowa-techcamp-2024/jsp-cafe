package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Post;
import woopaca.jspcafe.model.User;

import java.time.format.DateTimeFormatter;

public record PostDetailsResponse(String title, String content, String writer, String writtenAt, int viewCount,
                                  PageInfo pageInfo) {

    public static PostDetailsResponse of(Post post, PageInfo pageInfo, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return new PostDetailsResponse(post.getTitle(), post.getContent().trim(), user.getNickname(),
                post.getWrittenAt().format(formatter), post.getViewCount(), pageInfo);
    }
}
