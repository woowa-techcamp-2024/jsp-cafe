String.prototype.format = function () {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function (match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

function addReplyToDOM(reply) {
  let replyHtml = `
        <article class="article" id="answer-${reply.id}">
            <div class="article-header">
                <div class="article-header-thumb">
                    <img src="/resources/images/80-text.png" class="article-author-thumb" alt="">
                </div>
                <div class="article-header-text">
                    <a href="/users/${reply.userId}" class="article-author-name">${reply.username}</a>
                    <a class="article-header-time" title="퍼머링크">${reply.createdAt}</a>
                </div>
            </div>
            <div class="article-doc comment-doc">
                <p>${reply.contents}</p>
            </div>
            <div class="article-util">
                <ul class="article-util-list">
                    <li>
                        <form class="delete-answer-form" action="/replies/${reply.id}" method="POST">
                            <button type="submit" class="delete-answer-button">삭제</button>
                        </form>
                    </li>
                </ul>
            </div>
        </article>
    `;

  $('#top_holder').prepend(replyHtml);
}

let lastReplyId = null;

function loadReplies(number) {
  console.log(lastReplyId);
  $.ajax({
    url: `/replies/${number}${lastReplyId === null ? '' : '?id='
        + lastReplyId}`,
    type: 'GET',
    success: function (response) {
      console.log(`/replies/${number}${lastReplyId === null ? '' : '?id='
          + lastReplyId}`);
      console.log(response);
      response.data.forEach(reply => {
        addReplyToDOM(reply);
      });
      lastReplyId = response.data[response.numberOfElements - 1].id;
      if (response.hasNext) {
        $('#loadMoreButton').show();
      } else {
        $('#loadMoreButton').hide();
      }
    },
    error: function (error) {
      console.log(error);
    }
  });
}