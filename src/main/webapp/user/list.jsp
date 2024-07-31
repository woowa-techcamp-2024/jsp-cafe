<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../header.jsp" %>
<%@ include file="../navbar.jsp" %>
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
                <c:forEach var="user" items="${users}">
                    <tr>
                      <th scope="row">${user.id}</th>
                      <td>${user.userId}</td>
                      <td>${user.nickname} </a></td>
                      <td>${user.email}</td>
                      <td><a href="users/${user.id}" class="btn btn-success" role="button">수정</a></td>
                  </tr>
                </c:forEach>
              </tbody>
          </table>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>