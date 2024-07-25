<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오후 12:56
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
            <c:set var="message" value="${requestScope.loginException}"/>
            <div class="alert alert-danger" role="alert">
                <c:out value="${message} 다시 로그인 해주세요." escapeXml="false"/></div>
            <form name="question" method="post"
                  action="${pageContext.request.contextPath}/users/login">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" name="userId" placeholder="User ID"
                           required>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Password" required>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">로그인</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>