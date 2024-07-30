<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.example.domain.Article" %>
<%@ page import="org.example.constance.SessionName" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>글 수정</title>
    <link rel="stylesheet" href="/css/common.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container">
        <%@ include file="/common/header.jsp" %>
        <div class="info-box">
            <h2>글 수정</h2>
            <p>글의 제목과 내용을 수정할 수 있습니다.</p>
        </div>
        <%
            Article article = (Article) request.getAttribute("article");
        %>
        <form id="editForm">
            <input type="hidden" id="articleId" value="${article.articleId}">
            <div class="form-group">
                <label for="title">제목</label>
                <input name="title" type="text" id="title" value="${article.title}" placeholder="제목을 입력해주세요">
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea name="content" id="content" rows="10" placeholder="내용을 입력해주세요">${article.content}</textarea>
            </div>
            <button type="submit" class="btn">수정 완료</button>
        </form>
    </div>

    <script>
    $(document).ready(function() {
        $('#editForm').on('submit', function(e) {
            e.preventDefault();

            var articleId = $('#articleId').val();
            var title = $('#title').val();
            var content = $('#content').val();

            console.log(articleId + " "  + title + " " + content);

            $.ajax({
                url: '/api/articles/' + articleId,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({
                    title: title,
                    content: content
                }),
                success: function(response) {
                    alert('글이 성공적으로 수정되었습니다.');
                    window.location.href = '/articles/' + articleId;
                },
                error: function(xhr, status, error) {
                    alert('글 수정에 실패했습니다. 다시 시도해주세요.');
                }
            });
        });
    });
    </script>
</body>
</html>