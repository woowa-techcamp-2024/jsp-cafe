package woopaca.jspcafe.repository;

import woopaca.jspcafe.model.Post;

import java.util.List;

public interface PostRepository {

    void save(Post post);

    List<Post> findAll();
}
