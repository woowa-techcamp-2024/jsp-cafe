<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="/template/common/header.jsp" %>
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
                <%@ page import="java.util.List" %>
                <%@ page import="woowa.cafe.dto.UserInfo" %>
                    <%
                    List<UserInfo> users = (List<UserInfo>) request.getAttribute("users");
                    int count = 1;
                    %>
                <tbody>
                <% for (UserInfo user : users) { %>
                <tr>
                    <th scope="row"><%= count++ %>
                    </th>
                    <td><%= user.userId() %>
                    </td>
                    <td><%= user.name() %>
                    </td>
                    <td><%= user.email() %>
                    </td>
                    <td><a href="/user/<%=user.id()%>" class="btn btn-success" role="button">상세 조회</a></td>
                    <td><a href="/user/<%=user.id()%>/form" class="btn btn-success" role="button">수정</a></td>
                </tr>
                <% } %>
                </tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
</body>
</html>
