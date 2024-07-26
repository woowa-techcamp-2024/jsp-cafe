<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <h2 class="mt-5">User List</h2>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Username</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${userList}">
                <tr onclick="location.href='${pageContext.request.contextPath}/users/${user.id}'"
                    style="cursor: pointer;">
                    <td><c:out value="${user.username}"/></td>
                    <td><c:out value="${user.email}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/footer.jsp" %>
