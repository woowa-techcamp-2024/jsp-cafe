<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="org.example.domain.Article" %>
<%@ page import="org.example.domain.Reply" %>
<%@ page import="org.example.domain.User" %>
<%@ page import="java.util.List"%>
<%@ page import="org.example.constance.SessionName" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 상세보기</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/article/detail.css">
    <link rel="stylesheet" href="/css/reply/detail.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<%
    Article article = (Article) request.getAttribute("article");
    List<Reply> replies = (List<Reply>) request.getAttribute("replies");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     User currentUser = (User)session.getAttribute(SessionName.USER.getName());
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
                <% if (currentUser != null && currentUser.getUserId().equals(article.getUserId())) {%>
                    <a href="/articles/update-form/<%= article.getArticleId() %>"  class="btn">글 수정하기</a>
                    <button class="btn" id="deleteArticle">글 삭제하기</button>
                <% } %>
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
                    <% if (currentUser != null && currentUser.getUserId().equals(reply.getUserId())) {%>
                        <button class="btn delete-reply" data-reply-id="<%= reply.getReplyId() %>">삭제</button>
                    <% } %>
                </div>
                <% } %>
            </div>

            <button id="loadMoreReplies" class="btn" style="display: none;">더보기</button>

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
        // 글 삭제
        $('#deleteArticle').click(function() {
            if (confirm('정말로 이 글을 삭제하시겠습니까?')) {
                var articleId = <%= article.getArticleId() %>;
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

        var allReplies = [];
        var currentPage = 1;
        var repliesPerPage = 5;

        loadReplies();

        function loadReplies() {
            $.ajax({
                url: '/api/articles/replies/' + <%= article.getArticleId() %>,
                type: 'GET',
                success: function(replies) {
                    allReplies = replies;
                    updateReplyList(replies);
                },
                error: function(xhr, status, error) {
                    alert('댓글을 불러오는데 실패했습니다: ' + error);
                }
            });
        }

        function updateReplyList(replies) {
            var $replyList = $('.reply-list');
            $replyList.empty();

            var currentUserId = '<%= currentUser != null ? currentUser.getUserId() : "" %>';
            var startIndex = 0;
            var endIndex = currentPage * repliesPerPage;

            for(var i=startIndex; i < endIndex && i < allReplies.length; i++){
                var reply = allReplies[i];
                var deleteButton = '';
                if (reply.userId == currentUserId) {
                    deleteButton = '<button class="btn delete-reply" data-reply-id="' + reply.replyId + '">삭제</button>';
                }

                var replyHtml =
                    '<div class="reply" data-reply-id="' + reply.replyId + '">' +
                        '<div class="reply-header">' +
                            '<span class="reply-author">' + reply.author + '</span>' +
                            '<span class="reply-date">' + reply.createdDt + '</span>' +
                        '</div>' +
                        '<div class="reply-comment">' + reply.comment + '</div>' +
                        deleteButton +
                    '</div>';

                updateLoadMoreButton();
                $replyList.append(replyHtml);
            }

            // 삭제 버튼에 이벤트 리스너 다시 추가
            $('.delete-reply').click(deleteReplyHandler);
        }

        function updateLoadMoreButton() {
            var $loadMoreButton = $('#loadMoreReplies');
            if (currentPage * repliesPerPage < allReplies.length) {
                $loadMoreButton.show();
            } else {
                $loadMoreButton.hide();
            }
        }

        $('#loadMoreReplies').click(function() {
            currentPage++;
            updateReplyList();
        });

        $('#submitReply').click(function() {
            var comment = $('#replyComment').val();
            var articleId = <%= article.getArticleId() %>;

            $.ajax({
                url: '/api/replies',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ articleId: articleId, comment: comment }),
                success: function(response) {
                    $('#replyComment').val(''); // 입력 필드 비우기
                    loadReplies(); // 댓글 목록 새로고침
                },
                error: function(xhr, status, error) {
                    alert('댓글 작성에 실패했습니다: ' + error);
                }
            });
        });

        function deleteReplyHandler() {
            var replyId = $(this).data('reply-id');
            if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                $.ajax({
                    url: '/api/replies/' + replyId,
                    type: 'DELETE',
                    success: function(response) {
                        loadReplies(); // 댓글 목록 새로고침
                    },
                    error: function(xhr, status, error) {
                        alert('댓글 삭제에 실패했습니다(작성자만 삭제 가능합니다) ' + error);
                    }
                });
            }
        }

        // 동적으로 추가된 삭제 버튼에 대한 이벤트 위임
        $(document).on('click', '.delete-reply', deleteReplyHandler);
    });
    </script>
</body>
</html>