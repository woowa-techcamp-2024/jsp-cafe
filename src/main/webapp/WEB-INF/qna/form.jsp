<%--
  Created by IntelliJ IDEA.
  User: woowatech04
  Date: 2024. 7. 23.
  Time: 오후 5:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="kr">
<%@include file="../template/head.jsp" %>
<body>
<%@include file="../template/top-header.jsp" %>
<%@include file="../template/sub-header.jsp" %>

<div class="container" id="main">
    <div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
        <div class="panel panel-default content-main">
            <form name="question" method="post" action="/articles">
                <div class="form-group">
                    <label for="title">제목</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="제목"/>
                </div>
                <div class="form-group">
                    <label for="contents">내용</label>
                    <textarea name="contents" id="contents" rows="5" class="form-control"></textarea>
                </div>
                <button type="submit" class="btn btn-success clearfix pull-right">질문하기</button>
                <div class="clearfix"/>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<%@include file="../template/footer.jsp" %>
</body>
</html>