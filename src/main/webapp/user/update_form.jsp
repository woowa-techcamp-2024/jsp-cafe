<%@ page import="cafe.domain.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<%
    User user = (User) request.getAttribute("user");
    String id = (String) request.getAttribute("id");
%>>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <form name="article" method="post" action="/users/<%=id%>">
                <div class="form-group">
                    <label for="before-password">이전 비밀번호</label>
                    <input class="form-control" id="before-password" name="before-password" placeholder="">
                </div>
                <div class="form-group">
                    <label for="password">새 비밀번호</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="">
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" value="<%=user.getName()%>">
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input type="email" class="form-control" id="email" name="email" value="<%=user.getEmail()%>">
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정</button>
                <div class="clearfix" />
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
</body>
</html>