<%@ page import="codesquad.jspcafe.domain.user.payload.response.UserCommonResponse" %>
<%@ page import="codesquad.jspcafe.common.DefaultHTMLData" %><%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오전 12:27
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
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Profiles</h4></div>
            <div class="panel-body">
                <div class="well well-sm">
                    <div class="media">
                        <a class="thumbnail pull-left" href="#">
                            <img class="media-object"
                                 src="${pageContext.request.contextPath}/resources/images/80-text.png">
                        </a>
                        <div class="media-body">
                            <%
                                UserCommonResponse commonResponse = (UserCommonResponse) request.getAttribute(
                                        "user");
                            %>
                            <h4 class="media-heading"><%=commonResponse.getUsername()%>
                            </h4>
                            <p>
                                <a href="#" class="btn btn-xs btn-default"><span
                                        class="glyphicon glyphicon-envelope"></span>&nbsp;<%=commonResponse.getEmail()%>
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
<%=DefaultHTMLData.getScripts()%>
</body>
</html>
