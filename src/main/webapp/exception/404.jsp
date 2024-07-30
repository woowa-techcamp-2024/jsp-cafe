<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Not Found</title>
</head>
<%
    String message = (String) request.getAttribute("error");
%>
<body>
<h1>404 - Not Found</h1>
<p><%=message%></p>
</body>
</html>
