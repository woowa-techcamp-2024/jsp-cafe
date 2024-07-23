<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.entity.User" %>
<%@ page import="com.example.db.UserDatabase" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <title>유저 리스트</title>
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="../template/top-header.jsp"%>
<%@ include file="../template/sub-header.jsp"%>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    UserDatabase userDatabase = (UserDatabase) config.getServletContext().getAttribute("userDatabase");
                    List<User> userList = userDatabase.findAll();
                    for(User user : userList) {
                %>
                <tr>
                    <th scope="row"><%= user.id() %></th>
                    <td><a href="/users/<%= user.id() %>"><%= user.id() %></a></td>
                    <td><%= user.name() %></td>
                    <td><%= user.email() %></td>
                    <td><a href="#" class="btn btn-success" role="button">수정</a></td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
</body>
</html>
