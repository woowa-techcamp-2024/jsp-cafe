<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>에러 페이지</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/styles.css" rel="stylesheet">
</head>
<body>
<%@include file="/WEB-INF/component/header/header.jsp" %>
<%@include file="/WEB-INF/component/navigation/navigation.jsp" %>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-danger">
            <div class="panel-heading">
                <h3 class="panel-title">에러가 발생했습니다</h3>
            </div>
            <div class="panel-body">
                <p>문제가 발생했습니다. 관리자에게 문의하거나 나중에 다시 시도해주세요.</p>
                <p><strong>오류 메시지:</strong> <%= request.getAttribute("errorMsg") %></p>
                <p><strong>오류 메시지:</strong> <%= request.getAttribute("javax.servlet.error.message") %></p>
                <p><strong>오류 코드:</strong> <%= request.getAttribute("javax.servlet.error.status_code") %></p>
                <p><strong>요청 URI:</strong> <%= request.getAttribute("javax.servlet.error.request_uri") %></p>
                <p><strong>예외 타입:</strong> <%= request.getAttribute("javax.servlet.error.exception_type") %></p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">홈으로 돌아가기</a>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script src="/static/js/jquery-2.2.0.min.js"></script>
<script src="/static/js/bootstrap.min.js"></script>
<script src="/static/js/scripts.js"></script>
</body>
</html>
