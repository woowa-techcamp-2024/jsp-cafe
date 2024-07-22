<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello JSP</title>
</head>
<body>
<%
    String name = request.getParameter("name");
    if (name == null || name.isEmpty()) {
        name = "World";
    }
%>
<h1>Hello, <%= name %>!</h1>
</body>
</html>
