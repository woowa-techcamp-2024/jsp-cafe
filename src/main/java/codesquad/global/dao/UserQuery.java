package codesquad.global.dao;

import codesquad.common.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserQuery {
    Optional<UserResponse> findById(Long id);

    List<UserResponse> findAll(QueryRequest queryRequest);

    class QueryRequest extends PageRequest {
        public QueryRequest(Integer pageNumber, Integer pageSize) {
            super(pageNumber, pageSize);
        }
    }

    record UserResponse(
            Long id,
            String userId,
            String name,
            String email
    ) {
    }
}
