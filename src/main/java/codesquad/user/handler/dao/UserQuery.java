package codesquad.user.handler.dao;

import codesquad.common.http.request.PageRequest;
import codesquad.user.handler.dto.response.UserResponse;

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
}
