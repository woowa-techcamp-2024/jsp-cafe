<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="../snippet/meta_header.jsp"/>
<body>
<jsp:include page="../snippet/navigation.jsp"/>
<jsp:include page="../snippet/header.jsp"/>

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
                <c:if test="${sessionScope.signInUser != null}">
                    <tr>
                        <th scope="row">내 정보</th>
                        <td>${signInUser.userId}</td>
                        <td>${signInUser.name}</td>
                        <td>${signInUser.email}</td>
                        <td><a href="/users/edit/${signInUser.id}" class="btn btn-success" role="button">수정</a></td>
                    </tr>
                </c:if>
                <c:forEach var="user" items="${users.userResponses}" varStatus="status">
                    <tr>
                        <th scope="row">${status.count + (users.currentPage - 1) * 15}</th>
                        <td>${user.userId}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="row">
            <div class="col-md-3"></div>
            <div class="col-md-6 text-center">
                <ul class="pagination center-block" style="display:inline-block;">
                    <fmt:parseNumber var="totalPage" integerOnly="true"
                                     value="${(users.userCount + 14) / 15}"/>
                    <c:set var="currentPage" value="${users.currentPage}"/>

                    <fmt:parseNumber var="pageGroup" integerOnly="true" value="${(currentPage - 1) / 5}"/>
                    <c:set var="startPage" value="${pageGroup * 5 + 1}"/>
                    <c:set var="endPage" value="${Math.min(startPage + 4, totalPage)}"/>

                    <c:if test="${startPage > 1}">
                        <li><a href="?page=${startPage - 5}">«</a></li>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <li class="${i == currentPage ? 'active' : ''}">
                            <a href="?page=${i}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${endPage < totalPage}">
                        <li><a href="?page=${startPage + 5}">»</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="../snippet/script.jsp"/>
</body>
</html>
