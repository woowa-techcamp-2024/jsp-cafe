<%@ page import="codesquad.jspcafe.domain.user.payload.response.UserCommonResponse" %><%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오후 7:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <%
                String userId = (String) request.getAttribute("userId");
            %>
            <form name="question" method="post"
                  action="${pageContext.request.contextPath}/users/<%=userId%>/form">
                <input type="hidden" name="userId" value="<%=userId%>">
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Password" required>
                </div>
                <div class="form-group">
                    <label for="username">이름</label>
                    <input class="form-control" id="username" name="username" placeholder="Name">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="Email">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
