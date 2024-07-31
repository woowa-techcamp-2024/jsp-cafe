<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
</head>
<body>
<%@ include file="../Header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title">${post.title}</h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="https://graph.facebook.com/v2.3/100000059371774/picture"
                                 class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <a href="/users/${userId}" class="article-author-name">${post.writer}</a>
                            <a href="/questions/${post.id}" class="article-header-time" title="퍼머링크">
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc">
                        ${post.contents}
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <c:if test="${isAuthor}">
                                    <a class="link-modify-article" href="/questions/${post.id}/form">수정</a>
                                </c:if>
                            </li>
                            <li>
                                <c:if test="${isAuthor}">
                                    <button class="link-delete-article" onclick="deletePost(${post.id})">삭제</button>
                                </c:if>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <!-- 댓글 섹션 -->
                <div class="qna-comment">
                    <!-- 댓글 내용 -->
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script>
    function deletePost(postId) {
        if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
            var xhr = new XMLHttpRequest();
            xhr.open('DELETE', '/questions/' + postId, true);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    var redirectUrl = xhr.getResponseHeader('X-Redirect-Location');
                    if (redirectUrl) {
                        alert(xhr.responseText);
                        window.location.href = redirectUrl;
                    }
                } else {
                    alert('게시글 삭제에 실패했습니다: ' + xhr.responseText);
                }
            };
            xhr.onerror = function() {
                alert('네트워크 오류가 발생했습니다.');
            };
            xhr.send();
        }
    }
</script>
</script>
<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>