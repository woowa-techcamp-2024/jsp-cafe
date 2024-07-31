<%@ page import="org.example.demo.domain.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="/components/header.jsp"/>
<body>
<jsp:include page="/components/nav.jsp"/>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>id</th>
                    <th>userId</th>
                    <th>password</th>
                    <th>name</th>
                    <th>email</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<User> users = (List<User>) request.getAttribute("users");
                    for (User user : users) {
                %>
                <tr>
                    <th scope="row">
                        <a href="users/<c:out value="${user.id}" />">
                            <c:out value="${user.id}" />
                        </a>
                    </th>
                    <td><c:out value="${user.userId}" /></td>
                    <td><c:out value="${user.password}" /></td>
                    <td><c:out value="${user.name}" /></td>
                    <td><c:out value="${user.email}" /></td>
                    <td><a href="/users/<c:out value="${user.id}" />/form" class="btn btn-success" role="button">수정</a></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- script references -->
<jsp:include page="/components/footer.jsp"/>

</body>
</html>
