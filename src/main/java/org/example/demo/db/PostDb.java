package org.example.demo.db;

import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.PostCreateDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PostDb {
    private static Map<Long, Post> posts = new ConcurrentHashMap<>();

    public static Optional<Post> getPost(Long postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    public static void addPost(PostCreateDao dao) {
        //TODO User 로직 분리 필요
        User user = UserDb.getUserByUserId(dao.getWriter()).orElseThrow(() -> new NotFoundExceptoin("user not found"));
        Post post = new Post(generateNxtId(), user, dao.getTitle(), dao.getContents(), LocalDateTime.now());
        posts.putIfAbsent(post.getId(), post);
    }

    public static List<Post> getPosts() {
        return posts.values().stream().toList();
    }

    private static Long generateNxtId() {
        return (long) posts.size();
    }
}
