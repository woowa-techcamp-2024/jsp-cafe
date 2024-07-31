<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Illegal Argument Exception</title>
</head>
<body>
<%
    String message = (String) request.getAttribute("error");
%>
<h1>400 - Illegal Argument Exception</h1>
<p><%=message%>
</p>
</body>
</html>
