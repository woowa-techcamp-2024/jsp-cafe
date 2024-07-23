package org.example.demo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PostDb{
    private static Map<Long, Post> posts = new ConcurrentHashMap<>();

    public static Optional<Post> getPost(Long postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    public static void addPost(Post post) {
        post.setId(generateNxtId());
        posts.put(post.getId(), post);
    }

    public static List<Post> getPosts() {
        return posts.values().stream().toList();
    }

    private static Long generateNxtId() {
        return (long) posts.size();
    }
}
