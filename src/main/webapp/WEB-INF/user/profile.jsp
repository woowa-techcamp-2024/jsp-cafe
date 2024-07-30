<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.entity.User" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../template/head.jsp"%>
<body>
<%@ include file="../template/top-header.jsp"%>
<%@ include file="../template/sub-header.jsp"%>
<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object" src="../images/80-text.png">
                        </a>
                        <div class="media-body">
                            <%
                                User user = (User) request.getAttribute("user");
                            %>
                            <h4 class="media-heading"><%= user.name() %></h4>
                            <p>
                                <a href="mailto:<%= user.email() %>" class="btn btn-xs btn-default">
                                    <span class="glyphicon glyphicon-envelope"></span>&nbsp;<%= user.email() %>
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="../template/footer.jsp" %>
</body>
</html>
