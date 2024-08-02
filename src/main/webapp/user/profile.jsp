<%@ page import="domain.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<%@ include file="../header.jsp" %>
<body>
<%@ include file="../navigationbar.jsp" %>
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
                            <h4 class="media-heading"><c:out value="<%= user.getName() %>"/></h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>&nbsp;<%= user.getEmail() %>
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../footer.jsp" %>
</body>
</html>