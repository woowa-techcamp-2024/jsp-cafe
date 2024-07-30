<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>SLiPP Java Web Programming</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="<c:url value='/static/css/bootstrap.min.css'/>" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <link href="<c:url value='/static/css/styles.css'/>" rel="stylesheet">
    <script>
        function validateForm() {
            var isAuthor = ${isAuthor};
            if (!isAuthor) {
                alert("게시물 작성자만 수정할 수 있습니다.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<%@ include file="../Header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="<c:url value='/questions/${post.id}'/>"
                  onsubmit="return validateForm()">
                <!-- 기존 폼 내용 -->
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title"
                           value="<c:out value='${post.title}'/>" placeholder="제목"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control"><c:out
                            value='${post.contents}'/></textarea>
                </div>
                <c:if test="${isAuthor}">
                    <button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
                </c:if>
                <c:if test="${!isAuthor}">
                    <p class="text-danger">작성자만 수정할 수 있습니다.</p>
                </c:if>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<script src="<c:url value='/js/jquery-2.2.0.min.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>