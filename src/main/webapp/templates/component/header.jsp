<%@ page import="com.hyeonuk.jspcafe.member.domain.Member" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="${pageContext.request.contextPath}/templates/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="${pageContext.request.contextPath}/templates/css/styles.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-fixed-top header">
    <div class="col-md-12">
        <div class="navbar-header">

            <a href="${pageContext.request.contextPath}/" class="navbar-brand">SLiPP</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse1">
                <i class="glyphicon glyphicon-search"></i>
            </button>

        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse1">
            <form class="navbar-form pull-left">
                <div class="input-group" style="max-width:470px;">
                    <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
                    <div class="input-group-btn">
                        <button class="btn btn-default btn-primary" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                    </div>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-bell"></i></a>
                    <ul class="dropdown-menu">
                        <li><a href="https://slipp.net" target="_blank">SLiPP</a></li>
                        <li><a href="https://facebook.com" target="_blank">Facebook</a></li>
                    </ul>
                </li>
                <li><a href="${pageContext.request.contextPath}/list.jsp"><i class="glyphicon glyphicon-user"></i></a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="${pageContext.request.contextPath}/" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li><a href="${pageContext.request.contextPath}/user/profile"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i> Profile</a></li>
                <li class="nav-divider"></li>
                <li><a href="#"><i class="glyphicon glyphicon-cog" style="color:#dd1111;"></i> Settings</a></li>
            </ul>

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse2">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <%
                    if(session == null || session.getAttribute("member") == null){
                %>
                <li class="active"><a href="${pageContext.request.contextPath}/">Posts</a></li>>
                <li><a href="${pageContext.request.contextPath}/login" role="button">로그인</a></li>
                <li><a href="${pageContext.request.contextPath}/members/regist" role="button">회원가입</a></li>
                <%
                    }
                    else{
                        Member member = (Member)session.getAttribute("member");
                %>
                <li>
                    <h1><%=member.getNickname()%></h1>
                </li>
                <li>
                    <form method="post" action="${pageContext.request.contextPath}/logout">
                        <button type="submit">로그아웃</button>
                    </form>
                </li>
                <li><a href="${pageContext.request.contextPath}/members/<%=member.getMemberId()%>/form" role="button">개인정보수정</a></li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/templates/js/jquery-2.2.0.min.js"></script>
<script src="${pageContext.request.contextPath}/templates/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/templates/js/scripts.js"></script>
<script>
    // 공통된 설정을 포함한 fetch 함수
    async function fetchWrapper(url, options) {
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                // 서버 응답이 성공적이지 않다면 에러를 던짐
                const errorResponse = await response.json();
                throw new Error(errorResponse);
            }
            // 응답이 비어있지 않다면 JSON을 반환
            return response.json();
        } catch (error) {
            console.error('Fetch error:', error);
            throw error;
        }
    }

    async function get(endpoint) {
        return fetchWrapper(endpoint, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }
    async function post(endpoint, data) {
        return fetchWrapper(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });
    }
    async function del(endpoint) {
        return fetchWrapper(endpoint, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

</script>
</body>
</html>