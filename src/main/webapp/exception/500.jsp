<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Internal Server Error</title>
</head>
<%
    String message = (String) request.getAttribute("error");
%>
<body>
<h1>500 - Internal Server Error</h1>
<p>Something went wrong on the server. Please try again later.</p>
<p><%=message%></p>
</body>
</html>
