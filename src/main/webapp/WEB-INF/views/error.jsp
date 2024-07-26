<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>
<div class="container mt-5">
    <div class="alert alert-danger" role="alert">
        <h4 class="alert-heading">An error occurred!</h4>
        <p>Error Code: <c:out value="${requestScope['javax.servlet.error.status_code']}"/></p>
        <p>Message: <c:out value="${requestScope['javax.servlet.error.message']}"/></p>
        <hr>
        <p>If the problem persists, please contact support.</p>
        <a href="${pageContext.request.contextPath}/">Home</a>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
