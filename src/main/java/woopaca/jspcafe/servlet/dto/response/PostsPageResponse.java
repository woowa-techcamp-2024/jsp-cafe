package woopaca.jspcafe.servlet.dto.response;

import woopaca.jspcafe.model.Post;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record PostsPageResponse(List<PostsResponse> posts, Page page, int totalPosts) {

    public static PostsPageResponse of(List<Post> posts, List<String> writers, int currentPage, int totalPage, int totalPosts) {
        Page page = new Page(totalPage, currentPage);
        List<PostsResponse> postsResponseList = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            postsResponseList.add(PostsResponse.of(posts.get(i), writers.get(i)));
        }
        return new PostsPageResponse(postsResponseList, page, totalPosts);
    }

    public record PostsResponse(Long id, String title, String writer, String writtenAt, int viewCount) {

        public static PostsResponse of(Post post, String writer) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            return new PostsResponse(post.getId(), post.getTitle(), writer, post.getWrittenAt().format(formatter), post.getViewCount());
        }
    }

    public record Page(int totalPage, int currentPage) {
    }
}
