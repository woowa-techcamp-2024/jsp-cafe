<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="../common/header.jsp"/>

<div class="article-container">
    <h1 class="article-title">${article.title}</h1>

    <div class="article-meta">
        <span>작성자: ${article.authorNickname}</span>
        <%--        <span>작성일자: <fmt:formatDate value="${article.createdAt}" pattern="YYYY. MM. DD. HH:mm"/></span>--%>
        <span class="time">${article.createdAt}</span>
        <span>조회: ${article.hits}</span>
    </div>

    <div class="article-content">${article.content}</div>

    <div class="article-actions">
        <a href="${pageContext.request.contextPath}/articles/edit/${article.articleId}" class="edit-button">수정</a>
        <form action="${pageContext.request.contextPath}/articles/edit/${article.articleId}" method="post"
              style="display:inline;">
            <input type="hidden" name="_method" value="DELETE"/>
            <button class="delete-button">삭제</button>
        </form>
    </div>

    <div class="comment-section">
        <h3>댓글 <span id="comment-count">0</span>개</h3>
        <div id="comment-list">
            <!-- 댓글 목록이 여기에 동적으로 추가됩니다 -->
        </div>
    </div>

    <form id="comment-form" class="comment-form">
        <input type="hidden" name="articleId" value="${article.articleId}">
        <textarea name="content" placeholder="댓글을 입력하세요"></textarea>
        <button type="submit">등록</button>
    </form>

    <div class="navigation-buttons">
        <a href="${pageContext.request.contextPath}/articles" class="list-button">목록으로</a>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        const articleId = ${article.articleId};

        // 댓글 목록 불러오기
        function loadComments() {
            $.ajax({
                url: '/comments',
                method: 'GET',
                data: { articleId: articleId },
                dataType: 'json', // 명시적으로 JSON 응답을 기대함
                success: function(response) {
                    const commentList = $('#comment-list');
                    commentList.empty();

                    if (Array.isArray(response) && response.length > 0) {
                        response.forEach(function(comment) {
                            // JSP의 EL과 충돌을 피하기 위해 작은따옴표를 사용하고 + 연산자로 연결
                            const commentHtml = '<div class="comment" data-id="' + comment.replyId + '">' +
                                '<p class="comment-author">' + comment.userNickname + '</p>' +
                                '<p class="comment-content">' + comment.content + '</p>' +
                                '<p class="comment-date">' + comment.createdAt + '</p>' +
                                '<button class="delete-comment" data-id="' + comment.replyId + '">삭제</button>' +
                                '</div>';
                            console.log("commentHtml:", commentHtml);
                            commentList.append(commentHtml);
                        });
                        $('#comment-count').text(response.length);
                    } else {
                        commentList.append('<p>댓글이 없습니다.</p>');
                        $('#comment-count').text('0');
                    }
                },
                error: function(xhr, status, error) {
                    console.error("댓글 불러오기 실패:", error);
                    alert("댓글을 불러오는 데 실패했습니다.");
                }
            });
        }

        // 페이지 로드 시 댓글 불러오기
        console.log("페이지 로드 시 댓글 불러오기 시작");
        loadComments();
        console.log("페이지 로드 시 댓글 불러오기 종료");

        // 댓글 작성
        $('#comment-form').submit(function(e) {
            e.preventDefault();
            const content = $('textarea[name="content"]').val();
            $.ajax({
                url: '/comments',
                method: 'POST',
                data: { articleId: articleId, content: content },
                dataType: 'json',
                success: function(response) {
                    console.log("댓글 작성 성공:", response);
                    $('textarea[name="content"]').val('');
                    // 새로운 댓글을 화면에 즉시 추가
                    const newCommentHtml = '<div class="comment" data-id="' + response.replyId + '">' +
                        '<p class="comment-author">' + response.userNickname + '</p>' +
                        '<p class="comment-content">' + response.content + '</p>' +
                        '<p class="comment-date">' + response.createdAt + '</p>' +
                        '<button class="delete-comment" data-id="' + response.replyId + '">삭제</button>' +
                        '</div>';
                    $('#comment-list').append(newCommentHtml);
                    // 댓글 수 업데이트
                    const currentCount = parseInt($('#comment-count').text());
                    $('#comment-count').text(currentCount + 1);
                },
                error: function(xhr, status, error) {
                    console.error("댓글 작성 실패:", error);
                    alert("댓글 작성에 실패했습니다.");
                }
            });
        });

        // 댓글 삭제
        $(document).on('click', '.delete-comment', function() {
            const replyId = $(this).data('id');
            console.log("삭제하려는 댓글 ID: " + replyId);
            $.ajax({
                url: '/comments?articleId=' + articleId + '&replyId=' + replyId,
                method: 'DELETE',
                success: function(response) {
                    loadComments(); // 댓글 목록 새로고침
                },
                error: function(xhr, status, error) {
                    console.error("댓글 삭제 실패:", error);
                    alert("댓글 삭제에 실패했습니다.");
                }
            });
        });

    });
</script>



<%@ include file="../common/footer.jsp" %>
</body>
</html>