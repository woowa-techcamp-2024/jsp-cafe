<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.entity.User" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../template/head.jsp" %>
<body>
<%@ include file="../template/top-header.jsp" %>
<%@ include file="../template/sub-header.jsp" %>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>사용자 아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<User> userList = (List<User>)request.getAttribute("userList");
                    for (User user : userList) {
                %>
                <tr>
                    <th scope="row"><%= user.id() %>
                    </th>
                    <td><a href="/users/profile/<%= user.id() %>"><%= user.id() %>
                    </a></td>
                    <td><%= user.name() %>
                    </td>
                    <td><%= user.email() %>
                    </td>
                    <%--                    <td><a class="btn btn-success" href="/users/edit/<%= user.id() %>">수정</a></td>--%>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="../template/footer.jsp" %>
</body>
</html>
