package codesquad.javacafe.post.cache;

import codesquad.javacafe.post.dto.response.PostResponseDto;

import java.util.*;

public class PostCache {
    private static final PostCache instance = new PostCache();
    private static final int MAX_ENTRIES = 100;
    // LRU 캐시로 사용
    private static final Map<Long, PostResponseDto> cache = new LinkedHashMap<>(MAX_ENTRIES, 0.75f,true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, PostResponseDto> eldest) {
            return cache.size() > MAX_ENTRIES;
        }
    };

    private PostCache() {
    }

    public static PostCache getInstance() {
        return instance;
    }

    public void set(long id, PostResponseDto postResponseDto) {
        cache.put(id, postResponseDto);
    }

    public PostResponseDto get(long id) {
        cache.get(id);

        return cache.get(id);
    }

    public List<PostResponseDto> getCacheList() {
        return cache.values().stream().toList();
    }
}
