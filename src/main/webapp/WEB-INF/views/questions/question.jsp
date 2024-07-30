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
        <c:forEach var="reply" items="${replyList}">
            <div class="card mb-3">
                <div class="card-body">
                    <p class="card-text"><c:out value="${reply.content}"/></p>
                    <footer class="blockquote-footer text-end">
                        <c:out value="${reply.userName}"/>
                        <c:if test="${sessionScope.user != null and sessionScope.user.id == reply.userId}">
                            <button class="btn btn-danger btn-sm" onclick="deleteReply('<c:out value="${reply.id}"/>')">
                                Delete
                            </button>
                        </c:if>
                    </footer>
                </div>
            </div>
        </c:forEach>


        <hr>

        <h5>Leave a Comment</h5>
        <form id="commentForm" action="${pageContext.request.contextPath}/replies" method="post">
            <input type="hidden" id="articleId" name="articleId" value="<c:out value='${article.id}'/>">
            <div class="mb-3">
                <textarea class="form-control" id="content" name="content" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>


    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>

<script src="${pageContext.request.contextPath}/static/js/axios.min.js"></script>
<script>
    async function deleteArticle(articleId) {
        if (confirm("Are you sure you want to delete this article?")) {
            try {
                const response = await axios.delete(`${pageContext.request.contextPath}/questions/${article.id}`);
                if (response.status === 200) {
                    window.location.href = `${pageContext.request.contextPath}/questions`;
                } else {
                    alert('Failed to delete the article.');
                }
            } catch (error) {
                if (error.response.status === 403) {
                    alert(error.response.data);
                } else {
                    alert('Failed to delete the article.');
                }
            }
        }
    }
</script>
<script>
    async function deleteReply(replyId) {
        if (confirm("Are you sure you want to delete this comment?")) {
            try {
                const response = await axios.delete(`${pageContext.request.contextPath}/replies/` + replyId);
                if (response.status === 200) {
                    window.location.reload();
                } else {
                    alert('Failed to delete the comment.');
                }
            } catch (error) {
                if (error.response.status >= 400 && error.response.status < 500) {
                    alert(error.response.data);
                } else {
                    alert('Failed to delete the article.');
                }
            }
        }
    }
</script>
