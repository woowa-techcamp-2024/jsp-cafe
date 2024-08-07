package codesquad.comment.handler.dto.response;

import codesquad.common.http.response.PageInfo;
import codesquad.common.http.response.PageResponse;

import java.util.List;

public class PagedCommentResponse<T extends CommentResponse> extends PageResponse<CommentResponse> {
    public PagedCommentResponse(List<? extends T> content, PageInfo pageInfo) {
        super(content, pageInfo);
    }
}
