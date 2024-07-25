<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Error</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { width: 80%; margin: auto; padding-top: 20px; }
        .error-details { background-color: #ffdddd; border-left: 6px solid #f44336; margin: 20px 0; padding: 15px; }
        .error-details h2, .error-details p { margin: 0; }
    </style>
</head>
<body>
<div class="container">
    <h1>An error occurred</h1>
    <div class="error-details">
        <h2>Error Details:</h2>
        <p>Exception Type: <%= exception.getClass().getName() %></p>
        <p>Message: <%= exception.getMessage() %></p>
    </div>
</div>
</body>
</html>