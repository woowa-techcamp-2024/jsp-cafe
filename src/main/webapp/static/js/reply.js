// data
const replyInfos = {
    offset: 5,
    size: 5,
    data: [],
};

// function
function getMoreReply(questionId) {
    let url = `/question/${questionId}/reply?offset=${replyInfos.offset}&size=${replyInfos.size}`;

    fetch(url, {
        method: 'GET'
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                const pages = data.data;
                const replyList = pages.content;
                for (let replyInfo of replyList) {
                    addElementById('reply-box', replyHtml(replyInfo));
                }
                replyInfos.data.push(...replyList);
                replyInfos.offset += replyInfos.size;
                replyInfos.size = 5;
            })
        }
    })
}

function deleteReply(questionId, replyId) {
    let url = `/question/${questionId}/reply/${replyId}`

    fetch(url, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                deleteElementById("reply-" + data.data.replyId);
            })
            replyInfos.offset--;
            replyInfos.size++;
            replyInfos.data = replyInfos.data.filter(replyInfo => replyInfo.id !== replyId);
        } else {
            alert('삭제에 실패했습니다.');
        }
    });
}

function createReply(questionId) {
    let url = `/question/${questionId}/reply`;
    let content = document.getElementById("replyContent").value;

    let newForm = new FormData();
    newForm.append("content", content);
    let data = new URLSearchParams(newForm).toString();

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: data
    }).then(response => {
        if (response.ok) {
            response.json().then(data => {
                const replyInfo = data.data;
                incrementReplyCount(1);
                addElementById('reply-box', replyHtml(replyInfo));
            })
            replyInfos.size--;
            replyInfos.offset++;
            replyInfos.data = [data.data, ...replyInfos.data];
        } else {
            alert('답변에 실패했습니다.');
        }
    });
}
