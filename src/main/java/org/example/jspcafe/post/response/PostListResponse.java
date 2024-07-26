package org.example.jspcafe.post.response;

import java.util.List;

public record PostListResponse(
        int totalElements,
        int count,
        List<PostResponse> postList
) {
    public static PostListResponse of(int totalElements, List<PostResponse> postList) {
        return new PostListResponse(totalElements, postList.size(), postList);
    }
}
