function loadNextReply(questionId) {
    const lastReply = document.querySelector("#reply-list .article:last-child");
    const lastReplyId = lastReply ? lastReply.id.split("-")[1] : null;
    const lastReplyCreatedAt = lastReply ? lastReply.querySelector(".article-header-time").innerText : null;

    let url = `/replies?questionId=${questionId}`;
    if (lastReply) {
        url = `/replies?questionId=${questionId}&lastReplyId=${lastReplyId}&createdAt=${lastReplyCreatedAt}`
    }

    getAPI(url, renderNextReply);
}

function renderNextReply(response) {
    const replyList = document.getElementById("reply-list");
    response.data.forEach(item => {
        const replyItem = document.createElement('div');
        replyItem.innerHTML = renderReplyComponent(item);

        replyList.appendChild(replyItem.firstChild);
    })

    if (!response.cursor.hasNext) {
        document.getElementById("load-more-replies").style.display = "none";
    }
}

function renderReplyComponent(reply) {
    return `<article class="article" id="answer-${reply.replyId}">
                                        <div class="article-header">
                                            <div class="article-header-thumb">
                                                <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                     class="article-author-thumb" alt="">
                                            </div>
                                            <div class="article-header-text">
                                                <a href="/users/${reply.writer}"
                                                   class="article-author-name">${reply.writer}</a>
                                                <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                                        ${reply.createdAt}
                                                </a>
                                            </div>
                                        </div>
                                        <div class="article-doc comment-doc">
                                            <p>${reply.content}</p>
                                        </div>
                                        <div class="article-util">
                                            <ul class="article-util-list">
                                                <li>
                                                        <button id="delete-reply-btn" class="delete-answer-button"
                                                                onclick="deleteReply(${reply.replyId})">
                                                            삭제
                                                        </button>
                                                    </li>
                                            </ul>
                                        </div>
                                    </article>`
}

function createReply(questionId) {
    const content = $('#reply-content').val();
    postJsonAPI(`/replies`, {content, questionId}, afterCreateReply);
}

const afterCreateReply = (res) => {
    const replyList = document.getElementById('reply-list');
    const replyItem = document.createElement('div');
    replyItem.innerHTML = renderReplyComponent(res);

    replyList.appendChild(replyItem.firstChild);

    $('#reply-content').val('');
}

function deleteReply(replyId) {
    deleteAPI(`/replies/${replyId}`,
        (res) => document.getElementById(`answer-${replyId}`).remove())
}