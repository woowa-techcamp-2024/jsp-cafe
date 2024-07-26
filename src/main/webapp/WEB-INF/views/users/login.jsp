<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-6">
        <h2 class="mt-5">Login</h2>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <form action="${pageContext.request.contextPath}/user/login" method="post">
            <div class="mb-3">
                <label for="userId" class="form-label">Username</label>
                <input type="text" class="form-control" id="userId" name="userId"
                       value="${param.userId != null ? param.userId : ''}" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
