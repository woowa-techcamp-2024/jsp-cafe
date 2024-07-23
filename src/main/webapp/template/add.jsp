<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Addition Result</title>
</head>
<body>
<h1>Addition Result</h1>
<%
    // Get attributes from request
    Integer param1 = (Integer) request.getAttribute("param1");
    Integer param2 = (Integer) request.getAttribute("param2");

    // Initialize variables to store numbers and sum
    int number1 = 0;
    int number2 = 0;
    int sum = 0;

    // Check if attributes are not null and assign them to variables
    if (param1 != null && param2 != null) {
        number1 = param1;
        number2 = param2;
        sum = number1 + number2;
    }
%>

<%
    // Output the result if both attributes were valid numbers
    if (param1 != null && param2 != null) {
%>
<p>The sum of <%= number1 %> and <%= number2 %> is <%= sum %>.</p>
<%
} else {
%>
<p>Parameters are missing or invalid.</p>
<%
    }
%>

<form action="add.jsp" method="get">
    <label for="param1">Number 1:</label>
    <input type="text" id="param1" name="param1"><br>
    <label for="param2">Number 2:</label>
    <input type="text" id="param2" name="param2"><br>
    <input type="submit" value="Add">
</form>
</body>
</html>
