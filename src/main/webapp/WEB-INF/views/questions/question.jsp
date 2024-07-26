<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5"><c:out value="${article.title}"/></h2>
        <p class="text-muted">By
            <c:choose>
                <c:when test="${not empty user}">
                    <c:out value="${user.username}"/>
                </c:when>
                <c:otherwise>
                    Unknown
                </c:otherwise>
            </c:choose>
            on <c:out value="${article.createdDate}"/></p>
        <hr>
        <p><c:out value="${article.content}"/></p>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>
        <a href="${pageContext.request.contextPath}/questions" class="btn btn-secondary">Back to Post List</a>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>