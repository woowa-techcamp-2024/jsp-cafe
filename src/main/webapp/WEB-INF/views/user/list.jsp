<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="kr">
<jsp:include page="../snippet/meta-header.jsp"/>
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
                <c:forEach var="user" items="${users}" varStatus="status">
                    <tr>
                        <th scope="row">${status.count}</th>
                        <td>${user.userId}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td><a href="#" class="btn btn-success" role="button">수정</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="../snippet/script.jsp"/>
</body>
</html>
