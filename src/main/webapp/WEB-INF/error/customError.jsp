<%@ page import="codesquad.javacafe.common.exception.CustomException" %><%--
  Created by IntelliJ IDEA.
  User: seungh
  Date: 2024. 7. 27.
  Time: 오후 3:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<h1>An error occurred</h1>
<p>Sorry, an unexpected error has occurred. Please try again later.</p>

<!-- Display error details for debugging (optional) -->
<%
    var customException = (CustomException)request.getAttribute("exception");
	var errorMessage = customException.getErrorMessage();
	var HttpStatus = customException.getHttpStatus();
	var errorName = customException.getErrorName();

%>
<h2>Error Details:</h2>
<p><strong>Exception:</strong> <%= HttpStatus.getStatusCode() + " "+ errorName %></p>
<p><strong>Message:</strong> <%= errorMessage %></p>
<p><strong>Stack Trace:</strong></p>
<p><a href="/" >메인으로</a></p>
</body>
</html>