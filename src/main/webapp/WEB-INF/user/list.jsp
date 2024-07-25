<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="/WEB-INF/share/header.jsp" %>
<body>
<%@include file="/WEB-INF/share/navbar.jsp" %>
<%@include file="/WEB-INF/share/sub_navbar.jsp" %>
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
                    <th scope="row"><c:out value="${status.count}"/></th>
                    <td><a href="/users/<c:out value="${user.id}"/>"><c:out value="${user.userId}"/></a></td>
                    <td><c:out value="${user.name}"/></td>
                    <td><c:out value="${user.email}"/></td>
                    <td><a href="/users/profile/<c:out value="${user.id}"/>" class="btn btn-success" role="button">수정</a></td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../../js/jquery-2.2.0.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/scripts.js"></script>
</body>
</html>