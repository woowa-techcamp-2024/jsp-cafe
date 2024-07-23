<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>에러 페이지</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/styles.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-fixed-top header">
    <div class="col-md-12">
        <div class="navbar-header">

            <a href="../index.jsp" class="navbar-brand">SLiPP</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse1">
                <i class="glyphicon glyphicon-search"></i>
            </button>

        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse1">
            <form class="navbar-form pull-left">
                <div class="input-group" style="max-width:470px;">
                    <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
                    <div class="input-group-btn">
                        <button class="btn btn-default btn-primary" type="submit"><i
                                class="glyphicon glyphicon-search"></i></button>
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
                <li><a href="list.jsp"><i class="glyphicon glyphicon-user"></i></a></li>
            </ul>
        </div>
    </div>
</nav>
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
                <a href="../index.jsp" class="btn btn-primary">홈으로 돌아가기</a>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
</body>
</html>
