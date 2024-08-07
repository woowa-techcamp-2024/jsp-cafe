package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Page;
import woopaca.jspcafe.model.Post;

import java.util.Optional;

public interface PostRepository {

    void save(Post post);

    Optional<Post> findById(Long id);

    Page<Post> findToPage(int page, int limit);

    int countPublishedPosts();
}
