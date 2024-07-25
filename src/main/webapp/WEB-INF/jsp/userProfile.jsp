<%--
  Created by IntelliJ IDEA.
  User: KyungMin Lee
  Date: 24. 7. 23.
  Time: 오전 12:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE>
<html lang="ko">
<jsp:include page="/WEB-INF/jsp/component/headers.jsp"/>
<body>
<jsp:include page="/WEB-INF/jsp/component/navbar.jsp"/>

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
                            <c:set var="user" value="${requestScope.user}"/>
                            <h4 class="media-heading">
                                <c:out value="${user.username}" escapeXml="false"/>
                            </h4>
                            <p>
                                <a class="btn btn-xs btn-default" href="mailto:${user.email}">
                                    <span class="glyphicon glyphicon-envelope">
                                        <c:out value="${user.email}" escapeXml="false"/>
                                    </span>
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
<jsp:include page="/WEB-INF/jsp/component/scripts.jsp"/>
</body>
</html>
