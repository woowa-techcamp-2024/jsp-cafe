<%@ page contentType="text/html; charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="kr">
<head>
    <title>유저 목록 페이지</title>
    <%@include file="/WEB-INF/includes/head.jsp"%>
</head>
<body>
<%@include file="/WEB-INF/includes/navbar-fixed-top.jsp"%>
<%@include file="/WEB-INF/includes/navbar-default.jsp"%>
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
              <c:forEach var="user" items="${users}" varStatus="status">
                  <tr>
                      <th scope="row">${status.index + 1}</th>
                      <td>${user.userId}</td>
                      <td>${user.name}</td>
                      <td>${user.email}</td>
                      <td><a href="/userPage?action=update&seq=${user.userSeq}" class="btn btn-success" role="button">수정</a></td>
                  </tr>
              </c:forEach>
              </tbody>
          </table>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="/WEB-INF/includes/script-references.jsp"%>
</body>
</html>
