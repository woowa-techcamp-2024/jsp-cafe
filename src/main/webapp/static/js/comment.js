document.addEventListener('DOMContentLoaded', function() {
  let lastReplyId = -1;
  const replyCount = 5;

  // 메인 글 삭제 버튼
  document.getElementById('deleteButton').addEventListener('click', async function() {
    try {
      const response = await fetch(`/question/${articleId}`, { method: 'DELETE' });
      if (response.ok) {
        window.location.href = '/';
      } else {
        window.location.href = '/error/not-same-author.html';
      }
    } catch (error) {
      console.error('Error deleting article:', error);
    }
  });

  // 댓글 작성
  document.getElementById('replyForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const content = document.getElementById('replyContent').value;
    try {
      const response = await fetch('/reply', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: content, articleId: articleId })
      });
      if (response.ok) {
        lastReplyId = -1; // Reset lastReplyId after posting a new reply
        fetchReplies(lastReplyId, replyCount, true); // 기존 댓글을 지우고 새로 불러옴
      } else {
        alert('댓글 작성에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error posting reply:', error);
    }
  });

  // 댓글 삭제 및 목록 갱신 이벤트 위임
  document.querySelector('.replies').addEventListener('click', async function(event) {
    if (event.target.classList.contains('delete-reply-button')) {
      const replyId = event.target.getAttribute('data-reply-id');
      try {
        const response = await fetch(`/reply/${replyId}`, { method: 'DELETE' });
        if (response.ok) {
          lastReplyId = -1; // Reset lastReplyId after deleting a reply
          fetchReplies(lastReplyId, replyCount, true); // 기존 댓글을 지우고 새로 불러옴
        } else {
          alert('댓글 삭제에 실패했습니다.');
        }
      } catch (error) {
        console.error('Error deleting reply:', error);
      }
    }
  });

  // 더보기 버튼 클릭 이벤트
  document.getElementById('loadMoreReplies').addEventListener('click', function() {
    fetchReplies(lastReplyId, replyCount, false);
  });

  // 댓글 목록 갱신 함수
  async function fetchReplies(start = -1, count = 5, clearExisting = false) {
    try {
      const response = await fetch(`/question/${articleId}/replies?start=${start}&count=${count}`);
      if (response.ok) {
        const responseJson = await response.json();
        const replies = responseJson.replies;
        const totalCount = responseJson.totalCount;

        if (replies.length > 0) {
          lastReplyId = replies[replies.length - 1].replyId; // 마지막 댓글의 ID를 저장
        }

        updateReplies(replies, clearExisting);
        if (replies.length < count) {
          document.getElementById('loadMoreReplies').style.display = 'none';
        } else {
          document.getElementById('loadMoreReplies').style.display = 'block';
        }
      } else {
        alert('댓글 목록 갱신에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error fetching replies:', error);
    }
  }

  // 댓글 목록을 HTML로 업데이트하는 함수
  function updateReplies(replies, clearExisting = false) {
    const repliesContainer = document.querySelector('.replies');
    if (clearExisting) {
      repliesContainer.innerHTML = ''; // 기존 댓글을 지움
    }
    const repliesHtml = replies.map(reply => `
      <div class="reply">
        <p><strong>${reply.authorId}</strong>: ${reply.content}
        <button class="btn btn-danger btn-sm delete-reply-button" data-reply-id="${reply.replyId}" style="margin-left: 10px;">Delete</button>
        </p>
      </div>
    `).join('');
    repliesContainer.innerHTML += repliesHtml; // 새로운 댓글 추가
  }

  // 초기 댓글 목록 로드
  fetchReplies(-1, replyCount, true).then(() => console.log('Initial replies loaded'));
});
