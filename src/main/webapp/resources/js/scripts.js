String.prototype.format = function () {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function (match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

function escapeHtml(unsafe) {
  return unsafe
  .replace(/&/g, "&amp;")
  .replace(/</g, "&lt;")
  .replace(/>/g, "&gt;")
  .replace(/"/g, "&quot;")
  .replace(/'/g, "&#039;");
}

function addReplyToDOM(reply) {
  let replyHtml = `
        <article class="article" id="answer-${escapeHtml(reply.id)}">
            <div class="article-header">
                <div class="article-header-thumb">
                    <img src="/resources/images/80-text.png" class="article-author-thumb" alt="">
                </div>
                <div class="article-header-text">
                    <a href="/users/${escapeHtml(
      reply.userId)}" class="article-author-name">${escapeHtml(reply.username)}</a>
                    <a class="article-header-time" title="퍼머링크">${escapeHtml(
      reply.createdAt)}</a>
                </div>
            </div>
            <div class="article-doc comment-doc">
                <p>${escapeHtml(reply.contents)}</p>
            </div>
            <div class="article-util">
                <ul class="article-util-list">
                    <li>
                        <form class="delete-answer-form" action="/replies/${escapeHtml(
      reply.id)}" method="POST">
                            <button type="submit" class="delete-answer-button">삭제</button>
                        </form>
                    </li>
                </ul>
            </div>
        </article>
    `;

  $('#top_holder').prepend(replyHtml);
}