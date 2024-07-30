<%@ page import="org.example.jspcafe.post.model.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-post.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%
    Boolean isLogined = (Boolean) session.getAttribute("isLogined");
    if (isLogined == null || !isLogined) {
        response.sendRedirect("${pageContext.request.contextPath}/login");
        return;
    }
    String errorMessage = (String) request.getAttribute("errorMessage");
    Post post = (Post) request.getAttribute("post");
%>
<div class="container">
    <jsp:include page="/WEB-INF/jsp/common/header.jsp" />
    <main class="main-content">
        <div class="post-container">
            <h1 class="post-title">게시글 수정</h1>
            <form id="editPostForm" class="post-form">
                <div class="input-field">
                    <label for="title">제목</label>
                    <input
                            type="text"
                            id="title"
                            name="title"
                            placeholder="글의 제목을 입력하세요"
                            value="<%= post.getTitle().getValue() %>"
                            required
                    />
                </div>
                <div class="input-field">
                    <label for="content">내용</label>
                    <textarea
                            id="content"
                            name="content"
                            placeholder="글의 내용을 입력하세요"
                            required
                    ><%= post.getContent().getValue() %></textarea>
                </div>
                <button type="submit" class="submit-button">수정 완료</button>
            </form>
            <!-- 에러 메시지 모듈 포함 -->
            <jsp:include page="/WEB-INF/jsp/common/error-message.jsp">
                <jsp:param name="errorMessage" value="<%= errorMessage %>" />
            </jsp:include>
        </div>
    </main>
</div>
<script>
    $(document).ready(function() {
        $('#editPostForm').on('submit', function(event) {
            event.preventDefault();

            var postId = "<%= post.getPostId() %>";
            var title = $('#title').val();
            var content = $('#content').val();
            var actionUrl = `${pageContext.request.contextPath}/api/posts/` + postId;
            console.log(actionUrl);

            $.ajax({
                url: actionUrl,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({ title: title, content: content }),
                success: function(result) {
                    alert('게시글이 성공적으로 수정되었습니다.');
                    window.location.href = `${pageContext.request.contextPath}/posts/`+postId; // 성공 시 게시글 페이지로 리디렉션
                },
                error: function(xhr, status, error) {
                    var errorMessage = xhr.responseText || '게시글 수정 중 오류가 발생했습니다.';
                    $('#error-container').html(`
                        <div id="error-message" class="error-message">${errorMessage}</div>
                    `);
                    triggerErrorMessageScript();
                }
            });
        });

        function triggerErrorMessageScript() {
            const errorMessage = document.getElementById('error-message');
            if (errorMessage) {
                errorMessage.style.display = 'block';
                setTimeout(() => {
                    errorMessage.style.display = 'none';
                }, 3000); // 3초 후에 사라짐
            }
        }
    });
</script>
</body>
</html>
