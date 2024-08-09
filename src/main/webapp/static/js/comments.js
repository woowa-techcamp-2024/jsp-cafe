// comments.js
let lastCursor = null;
let hasMore = true;
const INITIAL_LOAD_LIMIT = 5;

$(document).ready(async () => {
    loadComments();
    loadUserComments();

    $('#commentForm').submit(async (e) => {
        e.preventDefault();
        submitComment();
    });

    $('#loadMoreComments').click(loadMoreComments);
});

const loadComments = async (append = false, limit = INITIAL_LOAD_LIMIT) => {
    try {
        const response = await axios.get(`${window.serverData.contextPath}/replies/${window.serverData.articleId}`, {
            params: { cursor: lastCursor, limit: limit }
        });
        console.log('Server response:', response.data);

        const comments = response.data.comments;
        lastCursor = response.data.nextCursor;
        hasMore = response.data.hasMore;

        let commentsHtml = '';
        comments.forEach((reply) => {
            commentsHtml += createCommentHtml(reply);
        });

        if (append) {
            $('#commentList').append(commentsHtml);
        } else {
            $('#commentList').html(commentsHtml);
        }

        $('#loadMoreComments').toggle(hasMore);
    } catch (error) {
        console.error('Error loading comments:', error);
        if (error.response) {
            console.error('Response data:', error.response.data);
            console.error('Response status:', error.response.status);
        }
        $('#commentList').html('<p>Error loading comments. Please try again later.</p>');
    }
};

const loadMoreComments = async () => {
    if (hasMore) {
        await loadComments(true);
    }
};

const createCommentHtml = (reply) => {
    console.log('Creating HTML for reply:', reply);
    const escapedContent = reply.content ? $('<div>').html(marked.parse(DOMPurify.sanitize(reply.content))).html() : '';
    const escapedUserName = reply.userName ? $('<div>').text(reply.userName).html() : '';
    let deleteButton = '';
    if (window.serverData.currentUserId && window.serverData.currentUserId === reply.userId) {
        deleteButton = `<button class="btn btn-danger btn-sm" onclick="deleteReply('${reply.id}')">Delete</button>`;
    }
    return `
        <div class="card mb-3" id="comment-${reply.id || ''}">
            <div class="card-body">
                <p class="card-text">${escapedContent}</p>
                <footer class="blockquote-footer text-end">
                    ${escapedUserName}
                    ${deleteButton}
                </footer>
            </div>
        </div>
    `;
};

const submitComment = async () => {
    const content = $('#content').val();

    try {
        const response = await axios.post(`${window.serverData.contextPath}/replies`, {
            content: content,
            articleId: window.serverData.articleId
        });
        $('#content').val('');

        // 새로 작성한 댓글을 사용자의 댓글 목록에 추가
        let reply = response.data;
        reply.userName = window.serverData.currentUserName;
        const newCommentHtml = createCommentHtml(reply);
        $('#userCommentList').append(newCommentHtml);
    } catch (error) {
        console.error('Error submitting comment:', error);
        alert(error.response.data);
    }
};

const deleteReply = async (replyId) => {
    if (confirm("Are you sure you want to delete this comment?")) {
        try {
            await axios.delete(`${window.serverData.contextPath}/replies/${replyId}`);
            $(`#comment-${replyId}`).remove();
        } catch (error) {
            console.error('Error deleting comment:', error);
            if (error.response && error.response.status >= 400 && error.response.status < 500) {
                alert(error.response.data);
            } else {
                alert('Failed to delete the comment.');
            }
        }
    }
};

const loadUserComments = async () => {
    try {
        const response = await axios.get(`${window.serverData.contextPath}/replies/users/${window.serverData.articleId}`);
        console.log('User comments response:', response.data);

        let userCommentsHtml = '';
        response.data.forEach((reply) => {
            userCommentsHtml += createCommentHtml(reply);
        });

        $('#userCommentList').html(userCommentsHtml);
    } catch (error) {
        console.error('Error loading user comments:', error);
        $('#userCommentList').html('<p>Error loading your comments. Please try again later.</p>');
    }
};

const deleteArticle = async (articleId) => {
    if (confirm("Are you sure you want to delete this article?")) {
        try {
            await axios.delete(`${window.serverData.contextPath}/questions/${articleId}`);
            window.location.href = `${window.serverData.contextPath}/questions`;
        } catch (error) {
            console.error('Error deleting article:', error);
            if (error.response && error.response.status === 403) {
                alert(error.response.data);
            } else {
                alert('Failed to delete the article.');
            }
        }
    }
};

// HTML에서 직접 호출되는 함수들을 전역 스코프에 할당
window.deleteReply = deleteReply;
window.deleteArticle = deleteArticle;