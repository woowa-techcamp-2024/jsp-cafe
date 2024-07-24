<%@ page import="com.woowa.cafe.domain.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="/WEB-INF/components/header.html" %>
<body>
<%@ include file="/WEB-INF/components/navbar-fixed-top-header.html" %>
<%@ include file="/WEB-INF/components/navbar-default.jsp" %>
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
                            <%
                                Member member = (Member) request.getAttribute("member");
                            %>
                            <h4 class="media-heading">"<%= member.getName()%>"</h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>"<%= member.getEmail()%>"</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@ include file="/WEB-INF/components/scirpt-refernces.html" %>
</body>
</html>
