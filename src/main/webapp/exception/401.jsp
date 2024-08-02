<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Unauthorized</title>
</head>
<%
    String message = (String) request.getAttribute("error");
%>
<body>
<h1>401 - Unauthorized</h1>
<p><%=message%>
</p>
</body>
</html>
