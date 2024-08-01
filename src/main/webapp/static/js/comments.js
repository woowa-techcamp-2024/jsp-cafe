// comments.js
$(document).ready(async function () {
    loadComments();

    $('#commentForm').submit(async function (e) {
        e.preventDefault();
        submitComment();
    });
});

async function loadComments() {
    try {
        const response = await axios.get(`${window.serverData.contextPath}/replies/${window.serverData.articleId}`);
        console.log('Server response:', response.data);
        let commentsHtml = '';
        response.data.forEach((reply) => {
            commentsHtml += createCommentHtml(reply);
        });
        $('#commentList').html(commentsHtml);
    } catch (error) {
        console.error('Error loading comments:', error);
        if (error.response) {
            console.error('Response data:', error.response.data);
            console.error('Response status:', error.response.status);
        }
        $('#commentList').html('<p>Error loading comments. Please try again later.</p>');
    }
}

function createCommentHtml(reply) {
    console.log('Creating HTML for reply:', reply);
    const escapedContent = reply.content ? $('<div>').text(reply.content).html() : '';
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
}

async function submitComment() {
    const content = $('#content').val();

    try {
        await axios.post(`${window.serverData.contextPath}/replies`, {
            content: content,
            articleId: window.serverData.articleId
        });
        $('#content').val('');
        loadComments();
    } catch (error) {
        console.error('Error submitting comment:', error);
        alert(error.response.data);
    }
}

async function deleteReply(replyId) {
    if (confirm("Are you sure you want to delete this comment?")) {
        try {
            await axios.delete(`${window.serverData.contextPath}/replies/${replyId}`);
            loadComments();
        } catch (error) {
            console.error('Error deleting comment:', error);
            if (error.response && error.response.status >= 400 && error.response.status < 500) {
                alert(error.response.data);
            } else {
                alert('Failed to delete the comment.');
            }
        }
    }
}

async function deleteArticle(articleId) {
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
}