package org.example.post.model.dto;

import java.util.List;

public class PagedPostsResult {
    private List<PostDto> posts;
    private int currentPage;
    private int totalPages;
    private int totalPosts;

    public PagedPostsResult(List<PostDto> posts, int currentPage, int totalPages, int totalPosts) {
        this.posts = posts;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalPosts = totalPosts;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
}