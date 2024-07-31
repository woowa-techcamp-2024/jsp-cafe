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
        <form action="${pageContext.request.contextPath}/articles/edit/${article.articleId}" method="post" style="display:inline;">
            <input type="hidden" name="_method" value="DELETE"/>
            <button class="delete-button">삭제</button>
        </form>
    </div>

    <div class="comment-section">
        <h3>댓글 ${comments.size()}개</h3>
        <c:forEach var="comment" items="${comments}">
            <div class="comment">
                <p class="comment-author">${comment.author}</p>
                <p class="comment-content">${comment.content}</p>
                <span class="time">${article.createdAt}</span>
<%--                <p class="comment-date"><fmt:formatDate value="${comment.createdAt}" pattern="YYYY. MM. DD. HH:mm"/></p>--%>
            </div>
        </c:forEach>
    </div>

    <form class="comment-form" action="${pageContext.request.contextPath}/comments/add" method="post">
        <input type="hidden" name="articleId" value="${article.articleId}">
        <textarea name="content" placeholder="댓글을 입력하세요"></textarea>
        <button type="submit">등록</button>
    </form>

    <div class="navigation-buttons">
<%--        <div class="prev-next">--%>
<%--            <a href="${pageContext.request.contextPath}/articles/${prevArticle.id}" class="prev-button">이전글: ${prevArticle.title}</a>--%>
<%--            <a href="${pageContext.request.contextPath}/articles/${nextArticle.id}" class="next-button">다음글: ${nextArticle.title}</a>--%>
<%--        </div>--%>
        <a href="${pageContext.request.contextPath}/articles" class="list-button">목록으로</a>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>