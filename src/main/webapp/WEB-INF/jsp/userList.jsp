<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 22.
  Time: 오후 8:20
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>
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
                <c:set var="idx" value="1" scope="page"/>
                <c:forEach var="userCommonResponse" items="${requestScope.userList}">
                    <tr>
                        <th scope="row">${idx}</th>
                        <c:set var="idx" value="${idx + 1}" scope="page"/>
                        <td>
                            <a href="/users/${userCommonResponse.userId}">
                                <c:out value="${userCommonResponse.userId}"/>
                            </a>
                        </td>
                        <td>
                            <c:out value="${userCommonResponse.username}"/>
                        </td>
                        <td>
                            <c:out value="${userCommonResponse.email}"/>
                        </td>
                        <td>
                            <button type="submit" class="btn btn-success"
                                    onclick="location.href='${pageContext.request.contextPath}/users/${userCommonResponse.userId}/form'">
                                수정
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- script references -->
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
