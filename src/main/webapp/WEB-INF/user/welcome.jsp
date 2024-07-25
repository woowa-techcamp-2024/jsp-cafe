<%@ include file="../common.jspf" %>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="../../static/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="../../static/css/styles.css" rel="stylesheet">
</head>
<body>
<%
    String userId = (String) session.getAttribute("userId");
    if (userId != null) {
%>
<h1>Welcome, <%= userId %>!</h1>
<form action="logout" method="post" style="display: inline;">
    <button type="submit">Logout</button>
</form>

<%
} else {
%>
<h1>Please <a href="/login">login</a> first</h1>
<%
    }
%>
<!-- script references -->
<script src="../../static/js/jquery-2.2.0.min.js"></script>
<script src="../../static/js/bootstrap.min.js"></script>
<script src="../../static/js/scripts.js"></script>
</body>
</html>