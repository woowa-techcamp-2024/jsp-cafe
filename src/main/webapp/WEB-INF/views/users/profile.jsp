<%@ page import="cafe.users.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<h2 class="mt-5">Profile</h2>
<div class="card">
    <div class="card-body">
        <h5 class="card-title">Username: <c:out value="${user.username}"/>
        </h5>
        <p class="card-text">Email: <c:out value="${user.email}"/>
        </p>
        <c:if test="${sessionScope.user.id == user.id}">
            <a href="${pageContext.request.contextPath}/users/edit/${user.id}" class="btn btn-primary">Edit Profile</a>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
