String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

function deleteElementById(id) {
    const element = document.getElementById(id);
    if (element) {
        element.remove();
    } else {
        console.log(`Element with id ${id} not found.`);
    }
}

function addElementById(id, htmlString) {
    const element = document.getElementById(id);
    if (element) {
        element.insertAdjacentHTML('beforeend', htmlString);
    } else {
        console.log(`Element with id ${id} not found.`);
    }
}

function replyHtml(replyInfo) {

    let replyContentHtml = "";

    for (let s of replyInfo.content.split("\n")) {
        replyContentHtml += "<p>" + s + "</p>";
    }

    const replyHtml = `<article id="reply-${replyInfo.id}" class="article">
                                <div class="article-header">
                                    <div class="article-header-thumb">
                                        <img src="https://graph.facebook.com/v2.3/1324855987/picture"
                                             class="article-author-thumb" alt="">
                                    </div>
                                    <div class="article-header-text">
                                        <a href="#" class="article-author-name">${replyInfo.authorName}
                                        </a>
                                        <a href="#" class="article-header-time" title="퍼머링크">
                                            ${replyInfo.createdDate}
                                        </a>
                                    </div>
                                </div>
                                <div class="article-doc comment-doc">
                                    ${replyContentHtml}
                                </div>
                                <div class="article-util">
                                    <ul class="article-util-list">
                                        <li>
                                            <a class="link-modify-article"
                                               href="/question/${replyInfo.questionId}/reply/${replyInfo.id}">수정</a>
                                        </li>
                                        <li>
                                            <input type="hidden" name="_method" value="DELETE">
                                            <button type="button" class="delete-answer-button"
                                                    onclick="deleteReply('${replyInfo.id}')">삭제
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </article> `

    return replyHtml
}

function incrementReplyCount(count) {
    const element = document.getElementById('reply-count');
    if (element) {
        let currentCount = parseInt(element.textContent, 10);
        if (!isNaN(currentCount)) {
            element.textContent = currentCount + count;
        } else {
            console.log('Current count is not a number.');
        }
    } else {
        console.log('Element with id reply-count not found.');
    }
}