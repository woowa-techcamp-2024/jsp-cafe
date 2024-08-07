$(document).ready(function() {
    let firstCommentId = Number.MAX_SAFE_INTEGER; // 처음으로 불러온 댓글의 ID
    const commentsPerPage = 5;
    let totalComments = 0;

    function formatDateTime(dateTime) {
        const date = new Date(dateTime);
        return `${date.getFullYear()}년 ${String(date.getMonth() + 1).padStart(2, '0')}월 ${String(date.getDate()).padStart(2, '0')}일 ${String(date.getHours()).padStart(2, '0')}시 ${String(date.getMinutes()).padStart(2, '0')}분 ${String(date.getSeconds()).padStart(2, '0')}초`;
    }

    function loadComments(postId, firstId = Number.MAX_SAFE_INTEGER, append = false) {
        $.ajax({
            url: `${contextPath}/api/posts/${postId}/comments?firstCommentId=${firstId}&size=${commentsPerPage}`,
            type: 'GET',
            success: function(response) {
                const comments = response.comments;
                totalComments = response.totalComments;

                if (!append) {
                    $('.comments-list').empty(); // 기존 댓글 목록 비우기
                }

                $('#commentCount').text(totalComments); // 댓글 수 업데이트
                comments.forEach(function(comment) {
                    var commentHtml = `
                    <div class="comment" data-comment-id="${comment.commentId}" data-post-id="${postId}">
                        <div class="comment-author">${comment.nickname}</div>
                        <div class="comment-content">${comment.content}</div>
                        <div class="comment-date">${formatDateTime(comment.createdAt)}</div>
                        ${isLogined && loggedInUserId == comment.userId ? `
                        <button class="edit-comment-button">수정</button>
                        <button class="delete-comment-button">삭제</button>
                        ` : ''}
                    </div>`;
                    $('.comments-list').append(commentHtml); // 새로운 댓글을 맨 위로 추가

                    // 처음으로 불러온 댓글 ID 업데이트
                    if (comment.commentId < firstCommentId) {
                        firstCommentId = comment.commentId;
                    }
                });

                // 디버깅을 위한 로그 추가
                console.log(`Total Comments: ${totalComments}`);
                console.log(`Comments Loaded: ${comments.length}`);
                console.log(`First Comment ID: ${firstCommentId}`);

                if (totalComments > $('.comment').length) {
                    console.log("Show Load More Button");
                    $('#loadMoreButton').show();
                } else {
                    console.log("Hide Load More Button");
                    $('#loadMoreButton').hide();
                }
            },
            error: function(xhr, status, error) {
                alert('댓글 목록을 불러오는 중 오류가 발생했습니다.');
            }
        });
    }

    // 페이지 로드 시 첫 페이지 댓글 목록 불러오기
    loadComments(postId);

    // 더보기 버튼 클릭 시 다음 페이지 댓글 불러오기
    $('#loadMoreButton').on('click', function() {
        loadComments(postId, firstCommentId, true);
    });

    // 댓글 작성 버튼 클릭 시 AJAX 요청 보내기
    $('.comment-form').on('submit', function(event) {
        event.preventDefault();

        var content = $(this).find('.comment-form-textarea').val();
        var actionUrl = `${contextPath}/api/posts/${postId}/comments`;

        $.ajax({
            url: actionUrl,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ content: content }),
            success: function(result) {
                alert('댓글이 성공적으로 작성되었습니다.');
                loadComments(postId, Number.MAX_SAFE_INTEGER, false); // 처음부터 로드하여 새 댓글이 맨 위에 오게 함
                $('.comment-form-textarea').val(''); // 입력 필드 초기화
            },
            error: function(xhr, status, error) {
                alert('댓글 작성 중 오류가 발생했습니다.');
            }
        });
    });

    // 댓글 삭제
    $(document).on('click', '.delete-comment-button', function(event) {
        event.preventDefault();

        if (!confirm('정말 삭제하시겠습니까?')) {
            return;
        }

        var commentId = $(this).closest('.comment').data('comment-id');
        var actionUrl = `${contextPath}/api/posts/${postId}/comments/${commentId}`;

        $.ajax({
            url: actionUrl,
            type: 'DELETE',
            success: function(result) {
                alert('댓글이 성공적으로 삭제되었습니다.');
                $(`[data-comment-id="${commentId}"]`).remove(); // 삭제된 댓글만 목록에서 제거
                totalComments--; // 총 댓글 수 감소
                $('#commentCount').text(totalComments); // 댓글 수 업데이트
            },
            error: function(xhr, status, error) {
                alert('댓글 삭제 중 오류가 발생했습니다.');
            }
        });
    });

    // 댓글 수정 버튼 클릭 시 수정 창 띄우기
    $(document).on('click', '.edit-comment-button', function(event) {
        event.preventDefault();

        var commentContent = $(this).closest('.comment').find('.comment-content').text();
        var commentId = $(this).closest('.comment').data('comment-id');

        var editFormHtml = `
        <div class="edit-comment-container">
            <textarea class="edit-comment-textarea">${commentContent}</textarea>
            <button class="save-comment-button" data-comment-id="${commentId}">저장</button>
            <button class="cancel-comment-button">취소</button>
        </div>
        `;

        $(this).closest('.comment').append(editFormHtml);
    });

    // 댓글 수정 저장 버튼 클릭 시 AJAX 요청 보내기
    $(document).on('click', '.save-comment-button', function(event) {
        event.preventDefault();

        var commentId = $(this).data('comment-id');
        var newContent = $(this).siblings('.edit-comment-textarea').val();
        var actionUrl = `${contextPath}/api/posts/${postId}/comments/${commentId}`;

        $.ajax({
            url: actionUrl,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent }),
            success: function(result) {
                alert('댓글이 성공적으로 수정되었습니다.');
                var commentElement = $(`[data-comment-id="${commentId}"]`);
                commentElement.find('.comment-content').text(newContent); // 수정된 내용 업데이트
                commentElement.find('.edit-comment-container').remove(); // 수정 창 닫기
            },
            error: function(xhr, status, error) {
                alert('댓글 수정 중 오류가 발생했습니다.');
            }
        });
    });

    // 댓글 수정 취소 버튼 클릭 시 수정 창 닫기
    $(document).on('click', '.cancel-comment-button', function(event) {
        event.preventDefault();
        $(this).closest('.edit-comment-container').remove();
    });
});
