<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="cafe.domain.entity.User" %>
<%@ page import="cafe.dto.UserDto" %>
<%@ include file="/WEB-INF/components/head.jsp" %>
<body>
<%@ include file="/WEB-INF/components/header.jsp"%>
<%@ include file="/WEB-INF/components/navigation.jsp"%>

<%
    UserDto userDto = (UserDto) request.getAttribute("user");
    User user = userDto.getUser();
%>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="/static/images/80-text.png">
                        </a>
                        <div class="media-body">
                            <h4 class="media-heading"><%=user.getName()%></h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span class="glyphicon glyphicon-envelope"></span><%=user.getEmail()%></a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/script.jsp" %>
	</body>
</html>