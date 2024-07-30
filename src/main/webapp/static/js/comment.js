document.addEventListener('DOMContentLoaded', function() {
  // 페이지가 로드되면 초기 댓글 목록 설정

  // 메인 글 삭제 버튼
  document.getElementById('deleteButton').addEventListener('click', function() {
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', `/question/${articleId}`, true);
    xhr.onload = function() {
      console.log(xhr.status);
      if (xhr.status === 200 || xhr.status === 204) {
        window.location.href = '/';
      } else {
        window.location.href = '/error/not-same-author.html';
      }
    };
    xhr.send();
  });

  // 댓글 작성
  document.getElementById('replyForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const content = document.getElementById('replyContent').value;
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/reply', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
      if (xhr.status === 201 || xhr.status === 200) {
        fetchReplies();
      } else {
        alert('댓글 작성에 실패했습니다.');
      }
    };
    xhr.send(JSON.stringify({ content: content, articleId: articleId }));
  });

  // 댓글 삭제 및 목록 갱신 이벤트 위임
  document.querySelector('.replies').addEventListener('click', function(event) {
    if (event.target.classList.contains('delete-reply-button')) {
      const replyId = event.target.getAttribute('data-reply-id');
      const xhr = new XMLHttpRequest();
      xhr.open('DELETE', `/reply/${replyId}`, true);
      xhr.onload = function() {
        if (xhr.status === 200) {
          fetchReplies();
        } else {
          alert('댓글 삭제에 실패했습니다.');
        }
      };
      xhr.send();
    }
  });

  // 댓글 목록 갱신 함수
  function fetchReplies() {
    const xhrReplies = new XMLHttpRequest();
    xhrReplies.open('GET', `/question/${articleId}/replies`, true);
    xhrReplies.onload = function() {
      if (xhrReplies.status === 200) {
        const responseJson = JSON.parse(xhrReplies.responseText);
        updateReplies(responseJson.replies);
      } else {
        alert('댓글 목록 갱신에 실패했습니다.');
      }
    };
    xhrReplies.send();
  }

  // 댓글 목록을 HTML로 업데이트하는 함수
  function updateReplies(replies) {
    const repliesHtml = replies.map(reply => `
      <div class="reply">
        <p><strong>${reply.authorId}</strong>: ${reply.content}
        <button class="btn btn-danger btn-sm delete-reply-button" data-reply-id="${reply.replyId}" style="margin-left: 10px;">Delete</button>
        </p>
      </div>
    `).join('');
    document.querySelector('.replies').innerHTML = repliesHtml;
  }
});
