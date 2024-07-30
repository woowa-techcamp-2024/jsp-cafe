<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.Article" %>
<%@ page import="org.example.domain.Reply" %>
<%@ page import="java.util.List"%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세보기</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/article/detail.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%
    Article article = (Article) request.getAttribute("article");
    List<Reply> replies = (List<Reply>) request.getAttribute("replies");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
%>
    <div class="container">
        <%@ include file="/common/header.jsp" %>
        <main>
            <h2 class="post-title"><%= article.getTitle() %></h2>
            <div class="post-info">
                <span>작성자: <%= article.getAuthor() %></span>
                <span>작성일: <%= article.getCreatedDt().format(formatter) %></span>
            </div>
            <div class="post-content">
                <p>
                    <%= article.getContent() %>
                </p>
            </div>
            <div class="action-buttons">
                <a class="btn" href="/">글 목록으로 돌아가기</a>
                <a href="/articles/update-form/<%= article.getArticleId() %>"  class="btn">글 수정하기</a>
                <button class="btn" id="deleteArticle">글 삭제하기</button>
            </div>

            <!-- 댓글 들 -->
            <h3>댓글</h3>
            <div class="reply-list">
                <% for(Reply reply : replies) { %>
                <div class="reply">
                    <div class="reply-header">
                        <span class="reply-author"><%= reply.getAuthor() %></span>
                        <span class="reply-date"><%= reply.getCreatedDt().format(formatter) %></span>
                    </div>
                    <div class="reply-comment"><%= reply.getComment() %></div>
                    <button class="btn delete-reply" data-reply-id="<%= reply.getReplyId() %>">삭제</button>
                </div>
                <% } %>
            </div>

            <!-- 댓글 작성 폼 -->
            <div class="reply-form">
                <h3>댓글 작성</h3>
                <textarea id="replyComment" placeholder="댓글 내용"></textarea>
                <button class="btn" id="submitReply">댓글 작성</button>
            </div>
        </main>
    </div>
    <script>
    $(document).ready(function() {
        $('#deleteArticle').click(function() {
            if (confirm('정말로 이 글을 삭제하시겠습니까?')) {
                var articleId = <%= article.getArticleId() %>;
                console.log("click " + articleId)
                $.ajax({
                    url: '/api/articles/' + articleId,
                    type: 'DELETE',
                       success: function(response) {
                       window.location.href = '/';
                    },
                    error: function(xhr, status, error) {
                        if(xhr.status === 400){
                            var errorResponse = JSON.parse(xhr.responseText);
                            alert(errorResponse.error);
                        } else if(xhr.status === 500){
                            var errorResponse = JSON.parse(xhr.responseText);
                            alert(errorResponse.error);
                        }
                    }
                });
            }
        });


        // 댓글 작성
        $('#submitReply').click(function() {
            var comment = $('#replyComment').val();
            var articleId = <%= article.getArticleId() %>;

            $.ajax({
                url: '/api/replies',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ articleId: articleId, comment: comment }),
                success: function(response) {
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert('댓글 작성에 실패했습니다: ' + error);
                }
            });
        });

        // 댓글 삭제
        $('.delete-reply').click(function() {
            var replyId = $(this).data('reply-id');
            if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                $.ajax({
                    url: '/api/replies/' + replyId,
                    type: 'DELETE',
                    success: function(response) {
                        location.reload();
                    },
                    error: function(xhr, status, error) {
                        alert('댓글 삭제에 실패했습니다(작성자만 삭제 가능합니다) ' + error);
                    }
                });
            }
        });
    });


    </script>
</body>
</html>