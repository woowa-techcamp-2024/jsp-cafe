<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@include file="/WEB-INF/component/header/header.jsp" %>
<%@include file="/WEB-INF/component/navigation/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <%
                User user = (User) request.getAttribute("user");
            %>
            <form name="question" method="post" action="${pageContext.request.contextPath}/users">
                <input type="hidden" name="_method" value="PUT">
                <div class="form-group">
                    <label for="id">id</label>
                    <input class="form-control" id="id" name="id" value="<%=user.getId()%>" type="hidden"/>
                </div>
                <div class="form-group">
                    <label for="userId">사용자 id</label>
                    <input class="form-control" id="userId" name="userId" value="<%=user.getUserId()%>" disabled/>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input class="form-control" id="password" name="password" value="<%=user.getPassword()%>" type="password"/>
                </div>
                <div class="form-group">
                    <label for="name">이름</label>
                    <input class="form-control" id="name" name="name" value="<%=user.getName()%>"/>
                </div>
                <div class="form-group">
                    <label for="email">이메일</label>
                    <input class="form-control" id="email" name="email" value="<%=user.getEmail()%>"/>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
</body>
</html>
