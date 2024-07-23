<%@ page import="codesquad.jspcafe.common.DefaultHTMLData" %><%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오전 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE>
<html lang="ko">
<head>
    <%=DefaultHTMLData.getHtmlHead()%>
</head>
<body>
<%=DefaultHTMLData.getNaviBar()%>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="${pageContext.request.contextPath}/users">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input class="form-control" id="userId" name="userId" placeholder="User ID">
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Password">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" placeholder="Name">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email"
                           placeholder="Email">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">회원가입</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>
<%=DefaultHTMLData.getScripts()%>
</body>
</html>