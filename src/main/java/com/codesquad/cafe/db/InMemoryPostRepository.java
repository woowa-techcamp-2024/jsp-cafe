package com.codesquad.cafe.db;

import com.codesquad.cafe.model.Post;
import com.codesquad.cafe.model.PostDetailsDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryPostRepository implements PostRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Long, Post> posts;

    private AtomicLong seq;

    public InMemoryPostRepository() {
        posts = new ConcurrentHashMap<>();
        seq = new AtomicLong(1);
    }

    @Override
    public Post save(Post post) {
        post.setId(seq.getAndIncrement());
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public List<Post> findAll() {
        return posts.values().stream().toList();
    }

    @Override
    public Page<Post> findByPage(int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException();
        }

        int totalElements = posts.size();
        int totalPage = (int) Math.ceil((double) posts.size() / pageSize);

        if (totalElements == 0 || pageNum > totalPage) {
            return Page.emptyPage(pageNum, pageSize, totalElements, totalPage);
        }

        int start = (pageNum - 1) * pageSize;
        List<Post> result = posts.values().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .skip(start)
                .limit(pageSize)
                .toList();

        return Page.of(result, pageNum, pageSize, result.size(), posts.size(), totalPage);
    }

    @Override
    public void deleteAll() {
        posts.clear();
    }

    @Override
    public List<PostDetailsDto> findAllPostWithDetail() {
        //todo
        return null;
    }
}
