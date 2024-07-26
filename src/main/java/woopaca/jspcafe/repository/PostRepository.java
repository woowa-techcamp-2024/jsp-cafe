package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    void save(Post post);

    List<Post> findAll();

    Optional<Post> findById(Long postId);
}
