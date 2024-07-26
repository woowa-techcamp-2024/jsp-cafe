<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 26.
  Time: 오전 12:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <h2><c:out value="${requestScope.statusCode}"/></h2>
            <div class="alert alert-danger" role="alert">
                <c:out value="${requestScope.message}"/></div>
            <button onclick="history.back()" class="btn btn-success">뒤로가기</button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
