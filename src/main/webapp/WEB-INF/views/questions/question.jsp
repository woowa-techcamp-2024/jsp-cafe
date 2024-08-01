<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5"><c:out value="${article.title}"/></h2>
        <p class="text-muted">By
            <c:out value="${article.userName}"/>
            on <c:out value="${article.createdDate}"/></p>
        <hr>
        <p><c:out value="${article.content}"/></p>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <a href="${pageContext.request.contextPath}/questions" class="btn btn-secondary">Back to Post List</a>

        <c:if test="${sessionScope.user != null and sessionScope.user.id == article.userId}">
            <a href="${pageContext.request.contextPath}/question/edit/${article.id}" class="btn btn-primary">Edit</a>
            <button class="btn btn-danger" onclick="deleteArticle('<c:out value="${article.id}"/>')">Delete</button>
        </c:if>

        <hr>

        <h4>Comments</h4>
        <div id="commentList">
            <!-- Comments will be dynamically loaded here -->
        </div>

        <hr>

        <h5>Leave a Comment</h5>
        <form id="commentForm">
            <input type="hidden" id="articleId" name="articleId" value="<c:out value='${article.id}'/>">
            <div class="mb-3">
                <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<script>
    // 서버 측 변수를 전역 객체로 설정
    window.serverData = {
        contextPath: '<c:out value="${pageContext.request.contextPath}"/>',
        articleId: <c:out value="${article.id}"/>,
        currentUserId: <c:out value="${sessionScope.user.id}"/>
    };
</script>
<script src="${pageContext.request.contextPath}/static/js/comments.js"></script>