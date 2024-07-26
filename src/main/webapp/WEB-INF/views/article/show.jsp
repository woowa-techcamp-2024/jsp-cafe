<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<%@ include file="../common/header.jsp" %>
<body>
<%@ include file="../common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-12">
        <div class="panel panel-default">
            <header class="qna-header">
                <h2 class="qna-title"><c:out value="${article.title}" /></h2>
            </header>
            <div class="content-main">
                <article class="article">
                    <div class="article-header">
                        <div class="article-header-thumb">
                            <img src="${pageContext.request.contextPath}/static/images/80-text.png" class="article-author-thumb" alt="">
                        </div>
                        <div class="article-header-text">
                            <c:choose>
                                <c:when test="${article.authorId != null}">
<%--                                    <a href="${pageContext.request.contextPath}/users/${articleResponse.authorId}" class="author">${articleResponse.authorNickname}</a>--%>
                                    <a href="/users/${article.authorId}" class="article-author-name"><c:out value="${article.authorNickname}" /></a>
                                </c:when>
                                <c:otherwise>
                                    <span class="author">${article.authorNickname}</span>
                                </c:otherwise>
                            </c:choose>
<%--                            <a href="/users/${article.authorId}" class="article-author-name"><c:out value="${article.authorNickname}" /></a>--%>
                            <a href="/articles/${article.articleId}" class="article-header-time" title="퍼머링크">
<%--                                <fmt:formatDate value="${article.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                                <span class="time">${article.createdAt}</span>
                                <i class="icon-link"></i>
                            </a>
                        </div>
                    </div>
                    <div class="article-doc comment-doc">
                        <c:out value="${article.content}" escapeXml="false" />
                    </div>
                    <div class="article-util">
                        <ul class="article-util-list">
                            <li>
                                <a class="link-modify-article" href="/articles/${article.articleId}/edit">수정</a>
                            </li>
                            <li>
                                <form class="form-delete" action="/articles/${article.articleId}" method="POST">
                                    <input type="hidden" name="_method" value="DELETE">
                                    <button class="link-delete-article" type="submit">삭제</button>
                                </form>
                            </li>
                            <li>
                                <a class="link-modify-article" href="/articles">목록</a>
                            </li>
                        </ul>
                    </div>
                </article>

                <div class="qna-comment">
                    <div class="qna-comment-slipp">
                        <p class="qna-comment-count"><strong>0</strong>개의 의견</p>
                        <div class="qna-comment-slipp-articles">
                            <!-- 댓글 목록을 여기에 추가 -->
                            <form class="submit-write">
                                <div class="form-group" style="padding:14px;">
                                    <textarea class="form-control" placeholder="Update your status"></textarea>
                                </div>
                                <button class="btn btn-success pull-right" type="button">답변하기</button>
                                <div class="clearfix"></div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>