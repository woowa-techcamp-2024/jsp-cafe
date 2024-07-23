<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="codesqaud.app.model.User" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/share/header.jsp" %>
<body>
<%@include file="/share/navbar.jsp" %>
<%@include file="/share/sub_navbar.jsp" %>
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
                    List<User> users = (List<User>) request.getAttribute("users");
                    for(int i = 0; i < users.size(); i++) {
                        User user = users.get(i);
                %>
                <tr>
                    <th scope="row"><%= i%></th>
                    <td><a href="/users/<%= user.getId()%>"><%= user.getUserId()%></a></td>
                    <td><%= user.getName()%></td>
                    <td><%= user.getEmail()%></td>
                    <td><a href="/<%= user.getUserId()%>" class="btn btn-success" role="button">수정</a></td>
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
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
</body>
</html>