<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i class="glyphicon glyphicon-chevron-down"></i></small></a>
            <ul class="nav dropdown-menu">
                <li><a href="/WEB-INF/user/profile.jsp"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i> Profile</a></li>
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
                <li class="active"><a href="/">Posts</a></li>
                <%
                    boolean signIn = (boolean) request.getAttribute("sign-in");
                    if (!signIn) {
                %>
                <li><a href="/users/sign-in" role="button">로그인</a></li>
                <li><a href="/users/sign-up" role="button">회원가입</a></li>
                <%
                    }
                    else {
                        String id = (String) request.getAttribute("id");
                %>
                <li>
                    <a href="#" onclick="event.preventDefault(); document.getElementById('logoutForm').submit();">로그아웃</a>
                </li>
                <form id="logoutForm" action="/users/sign-out" method="POST" style="display: none;"></form>
                <li><a href="/users/<%=id%>/edit" role="button">개인정보수정</a></li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</div>