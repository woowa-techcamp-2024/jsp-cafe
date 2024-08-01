<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
</head>
<body>
<%@ include file="../header.jsp" %>
<%@ include file="../nav.jsp" %>

<div class="container" id="main">
   <div class="col-md-10 col-md-offset-1">
      <div class="panel panel-default">
          <table class="table table-hover">
              <thead>
                <tr>
                    <th>#</th>
                    <th>사용자 아이디</th>
                    <th>닉네임</th>
                    <th>이메일</th>
                    <th></th>
                </tr>
              </thead>
              <tbody>
              <c:forEach var="user" items="${users}" varStatus="status">
                  <tr>
                      <th scope="row">${status.count}</th>
                      <td>${user.userId}</td>
                      <td><a href="/users/${user.userId}">${user.nickname}</a></td>
                      <td>${user.email}</td>
                      <td><a href="/users/${user.userId}/edit" class="btn btn-success" role="button">수정</a></td>
                  </tr>
              </c:forEach>
              </tbody>
          </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="${pageContext.request.contextPath}/js/jquery-2.2.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>
	</body>
</html>