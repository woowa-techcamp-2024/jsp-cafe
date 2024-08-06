function createReply(questionId) {
    const content = $('#reply-content').val();
    postJsonAPI(`/replies`, {content, questionId}, afterCreateReply);
}

const afterCreateReply = (res) => {
    console.log(res);

    const replyList = document.getElementById('reply-list');
    const replyTemplate = `<article class="article" id="answer-${res.replyId}">
                                        <div class="article-header">
                                            <div class="article-header-thumb">
                                                <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                                     class="article-author-thumb" alt="">
                                            </div>
                                            <div class="article-header-text">
                                                <a href="/users/${res.writer}"
                                                   class="article-author-name">${res.writer}</a>
                                                <a href="#answer-1434" class="article-header-time" title="퍼머링크">
                                                        ${res.createdAt}
                                                </a>
                                            </div>
                                        </div>
                                        <div class="article-doc comment-doc">
                                            <p>${res.content}</p>
                                        </div>
                                        <div class="article-util">
                                            <ul class="article-util-list">
                                                <li>
                                                        <button id="delete-reply-btn" class="delete-answer-button"
                                                                onclick="deleteReply(${res.replyId})">
                                                            삭제
                                                        </button>
                                                    </li>
                                            </ul>
                                        </div>
                                    </article>`

    const replyItem = document.createElement('div');
    replyItem.innerHTML = replyTemplate;

    replyList.appendChild(replyItem.firstChild);

    $('#reply-content').val('');
}

function deleteReply(replyId) {
    deleteAPI(`/replies/${replyId}`,
        (res) => document.getElementById(`answer-${replyId}`).remove())
}
