<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/template/component/head.jsp"></jsp:include>
<body>
<div>
    <jsp:include page="/template/component/script.jsp"></jsp:include>
</div>
<div>
    <jsp:include page="/template/component/nav.jsp"></jsp:include>
</div>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${article.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="${pageContext.request.contextPath}/users/${article.writer}" class="article-author-name">${article.writer}</a>
                            <a href="/questions/413" class="article-header-time" title="퍼머링크">
                                ${article.createdAt}
                                <i class="icon-link"></i>
                            </a>
                        </div>
                        <div class="article-util">
                            <ul class="article-util-list">
                                <li>
                                    <a class="link-modify-article" href="${pageContext.request.contextPath}/questions/${article.id}/form">수정</a>
                                </li>
                                <li>
                                    <button type="button" onclick="deleteArticle()" class="delete-answer-button">삭제</button>
                                </li>
                                <li>
                                    <a class="link-modify-article" href="${pageContext.request.contextPath}">목록</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="article-doc">
                        ${article.contents}
                    </div>
                </article>

                <%--댓글 부분--%>
                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <div class="qna-comment-slipp-articles">
                            <div class="qna-comment-slipp">
                                <p class="qna-comment-count"><strong id="commentCount">0</strong>개의 의견</p>
                                <div class="qna-comment-slipp-articles" id="commentList">
                                    <%-- 댓글 목록이 여기에 동적으로 삽입됩니다 --%>
                                </div>
                            </div>
                        </div>

                        <%-- 댓글 작성 폼 --%>
                        <form class="submit-write" id="commentForm">
                            <div class="form-group" style="padding:14px;">
                                <textarea class="form-control" id="commentContents" placeholder="댓글을 입력하세요"></textarea>
                            </div>
                            <button class="btn btn-success pull-right" type="button" onclick="addComment()">댓글 작성</button>
                            <div class="clearfix"></div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // 페이지 로드 시 댓글 목록 불러오기
    loadComments();

    function loadComments() {
        console.log("댓글을 불러옵니다.");
        $.ajax({
            url: '${pageContext.request.contextPath}/comments?articleId=${article.id}',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                updateCommentList(response);
            },
            error: function(xhr, status, error) {
                console.error("댓글 로딩 실패:", error);
            }
        });
    }

    function updateCommentList(comments) {
        var commentHtml = '';
        comments.forEach(function(comment) {
            console.log(comment);
            commentHtml +=
                '<article class="article" id="answer-' + comment.id + '">' +
                '<div class="article-header">' +
                '<div class="article-header-thumb">' +
                '<img src="https://graph.facebook.com/v2.3/1324855987/picture" class="article-author-thumb" alt="">' +
                '</div>' +
                '<div class="article-header-text">' +
                '<a href="/users/' + comment.writer + '" class="article-author-name">' + comment.writer + '</a>' +
                '<a href="#answer-' + comment.id + '" class="article-header-time" title="퍼머링크">' +
                comment.createdAt +
                '</a>' +
                '</div>' +
                '</div>' +
                '<div class="article-doc comment-doc">' +
                '<p>' + comment.contents + '</p>' +
                '</div>' +
                '<div class="article-util">' +
                '<ul class="article-util-list">' +
                // '<li>' +
                // '<a class="link-modify-article" href="javascript:void(0);" onclick="editComment(' + comment.id + ')">수정</a>' +
                // '</li>' +
                '<li>' +
                '<a href="javascript:void(0);" onclick="deleteComment(' + comment.id + ')">삭제</a>' +
                '</li>' +
                '</ul>' +
                '</div>' +
                '</article>';
        });
        $('#commentList').html(commentHtml);
        $('#commentCount').text(comments.length);
    }

    function addComment() {
        var contents = $("#commentContents").val();
        var data = {
            articleId: ${article.id},
            contents: contents
        };
        $.ajax({
            url: '${pageContext.request.contextPath}/comments',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                $("#commentContents").val(''); // 입력 필드 초기화
                loadComments(); // 댓글 목록 새로고침
            },
            error: function(xhr, status, error) {
                alert("댓글 추가에 실패했습니다.");
            }
        });
    }

    function deleteComment(commentId) {
        if (confirm("댓글을 삭제하시겠습니까?")) {
            $.ajax({
                url: '${pageContext.request.contextPath}/comments/' + commentId,
                type: 'DELETE',
                success: function(response) {
                    loadComments(); // 댓글 목록 새로고침
                },
                error: function(xhr, status, error) {
                    alert("댓글 삭제에 실패했습니다.");
                }
            });
        }
    }
</script>

<script>
    function deleteArticle() {
        var data = {
            title: $("#title").val(),
            contents: $("#contents").val()
        };
        if (confirm("게시글을 삭제하시겠습니까?")) {
            $.ajax({
            url: '${pageContext.request.contextPath}/questions/${article.id}',
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                console.log('Success:', response);
                window.location.href = '${pageContext.request.contextPath}';
            },
            error: function(xhr, status, error) {
                console.log('Error:', error);
                alert("게시글 삭제에 실패했습니다.");
            }
        })
        };
    }
</script>

</body>
</html>
