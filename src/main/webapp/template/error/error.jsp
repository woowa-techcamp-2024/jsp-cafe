<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/template/component/head.jsp"></jsp:include>

<head>
    <title>Exception Occurred</title>
    <style>
        .stack-trace {
            font-family: monospace;
            white-space: pre;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ddd;
            display: none;
        }
        .toggle-button {
            cursor: pointer;
            color: #007bff;
            text-decoration: underline;
        }
    </style>
    <script>
        function toggleStackTrace() {
            var stackTrace = document.getElementById('stackTrace');
            if (stackTrace.style.display === 'none') {
                stackTrace.style.display = 'block';
            } else {
                stackTrace.style.display = 'none';
            }
        }
    </script>
</head>
<body>
<div>
    <jsp:include page="/template/component/nav.jsp"></jsp:include>
</div>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    <h1>파일을 찾을 수 없습니다.</h1>
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 400}">
                    <h1>잘못된 요청입니다.</h1>
                    <c:out value="문제: ${pageContext.exception}" />
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">
                    <h1>처리 중 문제가 발생했습니다.</h1>
                    <c:out value="위치: ${pageContext.errorData.requestURI}" />
                    <br/>
                    <c:out value="문제: ${pageContext.exception}" />
                    <br/>
                    <p>
                        <span class="toggle-button" onclick="toggleStackTrace()">정보</span>
                    </p>
                    <div id="stackTrace" class="stack-trace">
                        <c:out value="${pageContext.exception}" />
                        <br/>
                        <c:forEach var="element" items="${pageContext.exception.stackTrace}">
                            <c:out value="${element}" />
                            <br/>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <h1>예외가 발생했습니다.</h1>
                </c:otherwise>
            </c:choose>
            <br/>

        </div>
    </div>
</div>

<div>
    <jsp:include page="/template/component/footer.jsp"></jsp:include>
</div>
</body>
</html>