<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="woowa.camp.jspcafe.service.dto.UserResponse" %>

<html>

<%@ include file="../common/header.jsp" %>

<body>

<%@ include file="../common/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-10 col-md-offset-1">
        <div class="panel panel-default">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>닉네임</th>
                    <th>이메일</th>
                    <th>회원가입일</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${requestScope.users}">
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/users/${user.id}"
                               class="btn btn-link">${user.nickname}</a></td>
                        <td>${user.email}</td>
                        <td>${user.registerAt}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/users/edit/${user.id}" class="btn btn-success"
                               role="button">수정</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

</body>
</html>