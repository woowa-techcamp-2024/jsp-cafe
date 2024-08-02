<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">

    <style>
        @font-face {
            font-family: 'Glyphicons Halflings';
            src: url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.eot');
            src: url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'),
            url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.woff') format('woff'),
            url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.ttf') format('truetype'),
            url('${pageContext.request.contextPath}/fonts/glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
        }
    </style>
    <style>
        .toast {
            visibility: hidden;
            max-width: 50%;
            margin: 0 auto;
            background-color: #333;
            color: #fff;
            text-align: center;
            border-radius: 5px;
            padding: 10px;
            position: fixed;
            z-index: 1;
            left: 0;
            right: 0;
            top: 60px;
            /*bottom: 30px;*/
            font-size: 17px;
            opacity: 0;
            transition: opacity 0.5s, visibility 0.5s;
        }

        .toast.show {
            visibility: visible;
            opacity: 1;
        }
    </style>
</head>
