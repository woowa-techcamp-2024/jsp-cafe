<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5">Create a New Post</h2>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
        </c:if>
        <form action="${pageContext.request.contextPath}/question/write" method="post">
            <div class="mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" class="form-control" id="title" name="title"
                       value="<c:out value="${param.title != null ? param.title : ''}" />" required>
            </div>
            <div class="mb-3">
                <label for="content" class="form-label">Content</label>
                <textarea class="form-control" id="content" name="content" rows="5"
                          required><c:out value="${param.content != null ? param.content : ''}" /></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
